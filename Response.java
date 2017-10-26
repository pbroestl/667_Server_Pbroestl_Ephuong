import java.io.DataOutputStream;
import java.io.IOException;

public interface Response {
  public void send(DataOutputStream out) throws IOException;

  public String getResponseMessage();
}