package streams.protocol;

public class FailureMessage {
  private final Throwable throwable;

  public FailureMessage(Throwable throwable) {
    this.throwable = throwable;
  }

  public Throwable getThrowable() {
    return throwable;
  }
}
