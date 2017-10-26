import java.lang.NullPointerException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException; 

public class Htaccess extends ConfigurationReader{
  
  private Htpassword userFile;
  private String authUserFile;
  private String authType;
  private String authName; 
  private String require;
  
  public Htaccess (String filePath) throws IOException{
    super(filePath);
    setHtaccessValues();

    if(isFullyLoaded())
      userFile = new Htpassword(authUserFile);
    
  }
  
  public void parseLine (String nextLine) throws NullPointerException {
    String [] splitLine = nextLine.split(" ", 2 );
    switch(splitLine[0].toUpperCase()){
      case "AUTHUSERFILE":
        authUserFile = splitLine[1].replace("\"","");
        if(authUserFile.charAt(0) == '/')
          authUserFile = authUserFile.substring(1, authUserFile.length());
        break;
      case "AUTHTYPE":
        authType = splitLine[1];
        break;
      case "AUTHNAME":
        authName = splitLine[1].replace("\"","");
        break;
      case "REQUIRE":
        require = splitLine[1];
        break;
    }
  }
  
  public void setHtaccessValues() throws IOException{
    while (hasNextLine()) {
      String readLine = nextLine();
      if (lineIsValidForParse(readLine))
        parseLine(readLine);
    }
  }
  public boolean isFullyLoaded(){
    return (authUserFile != null && authType != null && authName != null  && require != null ? true : false );
    
  }
  
  public boolean isAuthorized (String username, String password)throws NoSuchAlgorithmException {
    return userFile.isAuthorized(username, password);
  }
}