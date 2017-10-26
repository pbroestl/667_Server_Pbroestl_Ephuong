import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.security.NoSuchAlgorithmException; 

public class Resource {
  private Request request;
  private HttpdConf configuration;
  private String uri;
  private String absolutePath;
  private boolean isScript;
  private Htaccess htaccess;
  private boolean isProtected; 
  private boolean hasAuthHeader; 
  private boolean isAuthorized;
  
  public Resource (Request newRequest, HttpdConf confFile) throws IOException, NoSuchAlgorithmException {
    configuration = confFile;
    request = newRequest; 
    uri = request.getUri();
    
    generateAbsolutePath();  
    
    if(htaccessExists()) {
      checkAuthHeader(); 
    }   
  }
  
  @SuppressWarnings({"unchecked"})
  private void generateAbsolutePath(){   
    String docRoot = (String)configuration.getConfigurationOption("DOCUMENTROOT");
    List<String> dirIndex = (List<String>)configuration.getConfigurationOption("DIRECTORYINDEX");

    if(applyAliases("ALIAS")) {  
      isScript = false;
    }else if (applyAliases("SCRIPTALIAS")) {
      isScript = true; 
    }else {
      absolutePath =  docRoot + uri.substring(1, uri.length()) ; 
      isScript = false; 
    } 
    
    if (!uri.contains(".")) {
      if(!isScript){     
        if (uri.charAt(uri.length()-1) == '/')
          absolutePath = absolutePath + dirIndex.get(0);
        else 
          absolutePath = absolutePath + "/" + dirIndex.get(0);
      }
    }
  }
 
  @SuppressWarnings({"unchecked"}) 
  private boolean applyAliases(String aliasType) {
    Map <String, String> aliasMap =(HashMap< String, String>)configuration.getConfigurationOption(aliasType);
    
    for (String key : aliasMap.keySet()){
      if(uri.contains(key)){
        int aliasIndex = uri.indexOf(key) + key.length();
        absolutePath = aliasMap.get(key) + uri.substring(aliasIndex, uri.length());
        return true;
      }   
    }
    return false;  
  }
 
  private boolean htaccessExists() throws IOException {
    String cursor = "";
    String[] splitPath = absolutePath.split("/|\\\\");
    File tempFile = null;
   
    for (int i = 0; i < splitPath.length - 1 ; i++){
      cursor = cursor + "/" + splitPath[i];
      tempFile = new File( cursor + "/.htaccess");
      
      if (tempFile.exists() && !tempFile.isDirectory()){
        htaccess = new Htaccess(cursor + "/.htaccess");
        isProtected = true;
        return true;
      }    
    }
    isProtected = false; 
    return false;   
  }
  
  private void checkAuthHeader() throws NoSuchAlgorithmException {
    Map <String, String> headers = request.getHeaders();
    if (headers.containsKey("Authorization")) {
      hasAuthHeader = true;
      String[] headerValue = headers.get("Authorization").split(" ");
      String[] authorizationValues = parseAuthorization(headerValue[1]);

      if (htaccess.isAuthorized(authorizationValues[0], authorizationValues[1])){
        isAuthorized = true;
      } else {
        isAuthorized = false; 
      } 
    } else {
      hasAuthHeader = false; 
      isAuthorized = false;
    }                   
  }
  
  private String[] parseAuthorization(String credentials) {   
      byte[] decoded = Base64.getDecoder().decode(credentials);
      String decodedString = new String (decoded, StandardCharsets.UTF_8);
      return decodedString.split(":"); 
  }
  
  public String getAbsolutePath() {
    return absolutePath;
    
  }
   
  public boolean isScript(){
    return isScript;
  }
  
  public boolean isProtected() throws IOException {
     return isProtected;
  }
  
  public boolean hasAuthHeader() {
    return hasAuthHeader;
  }
  
  public boolean isAuthorized() {
    return isAuthorized;
  }
}