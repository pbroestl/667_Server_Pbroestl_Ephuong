import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.lang.StringBuilder;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Request {
  private String verb;
  private String uri;
  private String queryString;
  private String httpVersion;
  private Map<String, String> headers;
  private StringBuilder body;

  private final List<String> VERBS = Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE");

  public Request(InputStreamReader client) throws IOException, BadRequest {
    queryString = "";
    headers = new HashMap<String, String>();
    body = new StringBuilder();
    parse(new BufferedReader(client));
  }

  public void parse(BufferedReader requestMessage) throws IOException, BadRequest {
    String line;
    if((line = requestMessage.readLine()) != null) {
      String requestLine[] = line.split(" |\\?");
      loadRequestLine(requestLine);
    } else {
      throw new BadRequest("400 Bad Request");
    }

    while(!(line = requestMessage.readLine()).equals("") )
    {
      String headerFieldLine[] = line.split(":");
      headers.put(headerFieldLine[0], headerFieldLine[1].trim());
    }
      
    if(headers.containsKey("Content-Length")) {
      int contentLength = Integer.parseInt(headers.get("Content-Length"));
      char[] content = new char[contentLength];
      requestMessage.read(content, 0, contentLength);
      body.append(content);
    }
  }

  public void loadRequestLine(String[] line) throws BadRequest {
    if(VERBS.contains(line[0])) {
      verb = line[0];
      uri = line[1].replace("\"", "");
      httpVersion = line[line.length-1];
      if(line.length > 3) {
        queryString = line[2];
      }
    } else {
      throw new BadRequest("400 Bad Request");
    }
  }

  public String getUri() { return uri; }

  public StringBuilder getBody() { return body; }

  public String getVerb() { return verb; }

  public String getHttpVersion() { return httpVersion; }

  public String getQueryString() { return queryString; }

  public Map<String, String> getHeaders() { return headers; }
}