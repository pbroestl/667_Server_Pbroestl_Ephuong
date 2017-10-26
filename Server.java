import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

public class Server {
  private HttpdConf configuration;
  private MimeTypes mimeTypes;
  private Logger log;
  private ServerSocket socket;

  public void start() {
    try {
      log = new Logger("conf/log.txt");
      configuration = new HttpdConf("conf/httpd.conf");
      mimeTypes = new MimeTypes("conf/mime.types");

      int port = Integer.parseInt((String)configuration.getConfigurationOption("LISTEN"));
      socket = new ServerSocket(port);

      System.out.println("Waiting for client on port " + port);

      while(true) {
        Socket clientSocket = socket.accept();
        Thread worker = new Thread(new Worker(clientSocket, configuration, mimeTypes, log));
        worker.start();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } 
  }

  public static void main(String args[]) {
    Server server = new Server();
    server.start();
  }
}