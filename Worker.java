import java.net.Socket;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.lang.InterruptedException;

public class Worker implements Runnable {
  private HttpdConf configuration;
  private MimeTypes mimeTypes;
  private Socket client;
  private Logger log;

  public Worker(Socket socket, HttpdConf config, MimeTypes mime, Logger log) {
    configuration = config;
    mimeTypes = mime;
    client = socket;
    this.log = log;
  }

  public void run() {
    try {
      InputStreamReader in = new InputStreamReader(client.getInputStream());
      DataOutputStream out = new DataOutputStream(client.getOutputStream());
      Request request;

      try {
        request = new Request(in); 
        Resource resource = new Resource(request, configuration);
        Response response = ResponseFactory.getResponse(request, resource, mimeTypes);
        response.send(out);
        log.write(request, response);
        log.flushWriter();

      } catch (BadRequest badRequest) {
        badRequest.getMessage();
      } catch (NoSuchAlgorithmException nsae) {
        nsae.printStackTrace();
      } catch (InterruptedException ie) {
        ie.printStackTrace();
      }
      out.flush();
      
    } catch (IOException ioe) {
      ioe.printStackTrace();

    } finally {
      try {
        client.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }
}