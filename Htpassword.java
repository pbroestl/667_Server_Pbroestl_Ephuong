import java.util.Map;
import java.util.HashMap;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.lang.NullPointerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException; 
import java.util.Base64;
import java.lang.Exception;

public class Htpassword extends ConfigurationReader {
  private Map<String,String> users;
 
  public Htpassword (String filePath) throws IOException {
   super(filePath);
   users = new HashMap<String, String>();
   setUsers();
  }

  public void setUsers() throws IOException {
    while(hasNextLine()) {
      String readLine = nextLine();

      if (lineIsValidForParse(readLine))
        parseLine(readLine);
    }
  }
 
  public void parseLine (String nextLine) throws NullPointerException {
    String[] splitLine = nextLine.split(":|\\}");
    users.put(splitLine[0], splitLine[2]);   
  }
  
  public boolean isAuthorized(String username, String password) throws NoSuchAlgorithmException {
    if(users.containsKey(username)) {
      String storedPassword = users.get(username).replace(" ", "");
      MessageDigest md = MessageDigest.getInstance("SHA");
      byte[] hash = md.digest(password.getBytes());

      String encoded = new String(Base64.getEncoder().encode(hash));
      if (storedPassword.equals(encoded)) {
        return true;
      }
    }
    return false;
  }
}