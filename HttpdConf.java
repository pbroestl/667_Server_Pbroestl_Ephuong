import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;

public class HttpdConf extends ConfigurationReader {
  private Map<String, Object> configurationOptions;
  private Map<String, String> aliases;
  private Map<String, String> scriptAliases;
  private List<String> directoryIndex;

  private final String[] KEYS = { "SERVERROOT", "LISTEN", "DOCUMENTROOT", 
                                 "LOGFILE", "ALIAS", "SCRIPTALIAS", 
                                 "ACCESSFILENAME", "DIRECTORYINDEX" };

  private final String DEFAULT_ACCESS_FILENAME = ".htaccess";
  private final String DEFAULT_DIRECTORY_INDEX = "index.html";

  public HttpdConf(String filename) throws IOException {
    super(filename);
    configurationOptions = new HashMap<String, Object>();
    aliases = new HashMap<String, String>();
    scriptAliases = new HashMap<String, String>();
    directoryIndex = new ArrayList<String>();
    directoryIndex.add(DEFAULT_DIRECTORY_INDEX);

    createConfigurationOptions();
    
    configurationOptions.put("ACCESSFILENAME", DEFAULT_ACCESS_FILENAME);
    configurationOptions.put("DIRECTORYINDEX", directoryIndex);

    setConfigurationOptions();
  }

  private void createConfigurationOptions() {
   for(int i = 0; i < KEYS.length; i++) {
      configurationOptions.put(KEYS[i], null);
    }
  }

  public void setConfigurationOptions() throws IOException {
    while(hasNextLine()) {
      String readLine = nextLine();
      if(lineIsValidForParse(readLine))
        parseLine(readLine);
    }
  }

  public Object getConfigurationOption(String option) {
    return configurationOptions.get(option);
  }

  public void parseLine(String line) {
    String splitLine[] = line.split(" ");

    loadValues(splitLine, splitLine[0].toUpperCase());

  }

  public void loadValues(String[] values, String key) {
    switch (key) {
      case "ALIAS":
        aliases.put(values[1], values[2].replace("\"", ""));
        configurationOptions.put(key, aliases);
        break;
      case "SCRIPTALIAS":
        scriptAliases.put(values[1], values[2].replace("\"", ""));
        configurationOptions.put(key, scriptAliases);
        break;
      case "DIRECTORYINDEX":
        for(int i = 1; i < values.length; i++) {
          directoryIndex.add(values[i]);
        }
        configurationOptions.put(key, directoryIndex);
        break;
      default:
        configurationOptions.put(key, values[1].replace("\"", ""));
    }
  }
}