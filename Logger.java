import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;

public class Logger {
  private File file;
  private PrintWriter writer;
  public final String CRLF = "\r\n";

  public Logger(String filename) throws IOException {
    file = new File(filename);
    writer = new PrintWriter(new FileOutputStream(file, true));
  }

  public void write(Request request, Response response) {
    String requestLine = request.getVerb() + " " + request.getUri() + " " + 
      request.getHttpVersion() + CRLF;
    System.out.println(requestLine);

    String responseMessage = response.getResponseMessage();
    System.out.println(responseMessage);

    String logMessage = requestLine + responseMessage;
    writer.write(logMessage);
  }

  public void flushWriter() {
    writer.flush();
  }
}