package streams.protocol;

public class ProcessMessage {
  private final String message;

  public ProcessMessage(String message) {
    this.message = message;
    }

  public String getMessage() {
    return message;
  }
}
