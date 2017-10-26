import java.util.Map;
import java.util.HashMap;
import java.lang.NullPointerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Exception;

public class MimeTypes extends ConfigurationReader {

  private Map<String, String> mimeTypesMap;
  
  public MimeTypes(String fileName)throws FileNotFoundException, IOException{
    super(fileName);
    mimeTypesMap = new HashMap<String, String>();
    setMimeTypeMap();
  }

  public void setMimeTypeMap () throws IOException{
    while(hasNextLine()) {
      String readLine = nextLine();
      if (lineIsValidForParse(readLine))
        parseLine(readLine);
    }
  }

  public void parseLine (String nextLine) throws NullPointerException {
    String[] splitLine = nextLine.split(" |\\t");
    for (int i = 1; i < splitLine.length; i++){
      mimeTypesMap.put(splitLine[i], splitLine[0]);
    }
  }

  public String findMimeType (String extension) {
    if (keyExists(extension))
      return mimeTypesMap.get(extension);
    else 
      return "text/plain";
  }

  public boolean keyExists(String extension){
    return mimeTypesMap.containsKey(extension);
  }
}