import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.StringBuilder;
import java.io.IOException;
import java.util.Date;
import java.lang.ProcessBuilder;
import java.lang.InterruptedException;

public class GetResponse implements Response {
  public final int CODE = 200;
  public final String REASON_PHRASE = "OK";
  public final String CRLF = "\r\n";
  public final String server = "CSC667/Paul and Elaine's Server";

  private String responseMessage;
  byte[] body;
  int fileSize;
  

  public GetResponse(Request request, Resource resource, MimeTypes mime) throws IOException, InterruptedException {
    String resourcePath = resource.getAbsolutePath();
    File file = new File(resourcePath);
    FileInputStream fis = new FileInputStream(file);
    Date date = new Date();
    Date lastModified = new Date(file.lastModified());
    fileSize = (int)file.length();
    String extension = 
      resourcePath.substring(resourcePath.indexOf(".")+1, resourcePath.length());
    String contentTypeHeader;

    if(resource.isScript()) {
      ProcessBuilder pb = new ProcessBuilder(resource.getAbsolutePath(), request.getQueryString());
      Process process = pb.start();

      process.waitFor();

      BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
      StringBuilder sb = new StringBuilder();

      String line = br.readLine();
      String[] splitLine = line.split(";");
      contentTypeHeader = splitLine[0] + CRLF;

      while((line = br.readLine()) != null) {
        sb.append(line);
      }
      br.close();

      body = sb.toString().getBytes();

    } else {
      contentTypeHeader = "Content-Type: " + mime.findMimeType(extension) + CRLF;
      body = new byte[fileSize];
      fis.read(body);
      fis.close();
    }
    
    String statusLine = request.getHttpVersion() + " " + CODE + " " + REASON_PHRASE + CRLF;
    String dateHeader = "Date: " + date + CRLF;
    String serverHeader = "Server: " + server + CRLF;
    String lastModifiedHeader = "Last-Modified: " + lastModified + CRLF;
    String contentLengthHeader = "Content-Length: " + fileSize + CRLF;

    responseMessage = statusLine + dateHeader + serverHeader +
      lastModifiedHeader + contentLengthHeader + contentTypeHeader + CRLF;
  }

  public void send(DataOutputStream out) throws IOException {
    out.writeBytes(responseMessage);
    out.write(body, 0, fileSize);
  }

  public String getResponseMessage() { return responseMessage; }

}