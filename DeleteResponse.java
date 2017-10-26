import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class DeleteResponse implements Response {
  public final int CODE = 204;
  public final String REASON_PHRASE = "No Content";
  public final String CRLF = "\r\n";
  public final String server = "CSC667/Paul and Elaine's Server";

  private String responseMessage;

  public DeleteResponse(Request request, Resource resource) throws IOException {
    String resourcePath = resource.getAbsolutePath();
    File file = new File(resourcePath);
    Date date = new Date();
    
    String statusLine;
    String dateHeader;
    String serverHeader;
    if(file.delete()) {
      statusLine = request.getHttpVersion() + " " + CODE + " " + REASON_PHRASE + CRLF;
      dateHeader = "Date: " + date + CRLF;
      serverHeader = "Server: " + server + CRLF;

      responseMessage = statusLine + dateHeader + serverHeader + CRLF;
    }
  }

  public void send(DataOutputStream out) throws IOException {
    out.writeBytes(responseMessage);
  }

  public String getResponseMessage() { return responseMessage; }
} 