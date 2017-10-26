import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class HeadResponse implements Response {
  public final int CODE = 200;
  public final String REASON_PHRASE = "OK";
  public final String CRLF = "\r\n";
  public final String server = "CSC667/Paul and Elaine's Server";

  private String responseMessage;

  public HeadResponse(Request request, Resource resource, MimeTypes mime) throws IOException {
    String resourcePath = resource.getAbsolutePath();
    File file = new File(resourcePath);
    Date date = new Date();
    Date lastModified = new Date(file.lastModified());
    int fileSize = (int)file.length();
    String extension = 
      resourcePath.substring(resourcePath.indexOf(".")+1, resourcePath.length());

    String statusLine = request.getHttpVersion() + " " + CODE + " " + REASON_PHRASE + CRLF;
    String dateHeader = "Date: " + date + CRLF;
    String serverHeader = "Server: " + server + CRLF;
    String lastModifiedHeader = "Last-Modified: " + lastModified + CRLF;
    String contentLengthHeader = "Content-Length: " + fileSize + CRLF;
    String contentTypeHeader = "Content-Type: " + mime.findMimeType(extension) + CRLF;

    responseMessage = statusLine + dateHeader + serverHeader +
      lastModifiedHeader + contentLengthHeader + contentTypeHeader + CRLF;
  }

  public void send(DataOutputStream out) throws IOException {
    out.writeBytes(responseMessage);
  }

  public String getResponseMessage() { return responseMessage; }
}