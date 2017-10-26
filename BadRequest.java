import java.lang.Exception;

public class BadRequest extends Exception {
  public BadRequest(String message) {
    super(message);
  }

  public String getMessage() {
    return super.getMessage();
  }
}