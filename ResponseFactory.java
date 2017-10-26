import java.io.File;
import java.io.IOException;
import java.lang.InterruptedException;

public class ResponseFactory {
  public static Response getResponse( Request request, Resource resource, MimeTypes mimeTypes) throws IOException, InterruptedException {
    String verb = request.getVerb();
    File file = new File(resource.getAbsolutePath());

    if(resource.isProtected()) {
      if(!resource.hasAuthHeader() ) {
        return new UnauthorizedResponse(request, resource);
      } else { 
        if(!resource.isAuthorized() ) {
          return new ForbiddenResponse(request, resource);
        }
      }       
    } 
    if(!file.exists()) {
      if(!verb.equals("PUT"))
        return new NotFoundResponse(request, resource);
    }
    
    switch(verb) {
      case "GET":
        return new GetResponse(request, resource, mimeTypes);
      case "HEAD": 
        return new HeadResponse(request, resource, mimeTypes);
      case "POST": 
        return new GetResponse(request, resource, mimeTypes);
      case "PUT": 
        return new PutResponse(request, resource, mimeTypes);
      case "DELETE":
        return new DeleteResponse(request, resource);
      default:
        return new BadRequestResponse(request, resource);
    }
  }
}