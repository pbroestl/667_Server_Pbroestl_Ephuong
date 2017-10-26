import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class PutResponse implements Response {
  public final int CODE = 201;
  public final String REASON_PHRASE = "Created";
  public final String CRLF = "\r\n";
  public final String server = "CSC667/Paul and Elaine's Server";

  private String responseMessage;
  byte[] body;
  int fileSize;

  public PutResponse(Request request, Resource resource, MimeTypes mime) throws IOException {
    String resourcePath = resource.getAbsolutePath();
    File file = new File(resourcePath);
    FileOutputStream outStream = new FileOutputStream(file);
    Date date = new Date();
    String extension = 
      resourcePath.substring(resourcePath.indexOf(".")+1, resourcePath.length());
    
    body = request.getBody().toString().getBytes();
    fileSize = body.length;

    outStream.write(body);
    outStream.close();

    String statusLine = request.getHttpVersion() + " " + CODE + " " + REASON_PHRASE + CRLF;
    String dateHeader = "Date: " + date + CRLF;
    String serverHeader = "Server: " + server + CRLF;
    String locationHeader = "Location: " + resourcePath + CRLF;
    String contentLengthHeader = "Content-Length: " + fileSize + CRLF;
    String contentTypeHeader = "Content-Type: " + mime.findMimeType(extension) + CRLF;

    responseMessage = statusLine + dateHeader + serverHeader + locationHeader +
      contentLengthHeader + contentTypeHeader + CRLF;
  }

  public void send(DataOutputStream out) throws IOException {
    out.writeBytes(responseMessage);
    out.write(body, 0, fileSize);
  }

  public String getResponseMessage() { return responseMessage; }
}