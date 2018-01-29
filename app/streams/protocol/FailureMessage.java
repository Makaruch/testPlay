package streams.protocol;

/**
 * Сообщение об ошибке в стриме. Отправлается поставщиком.
 */
public class FailureMessage {
  private final Throwable throwable;

  public FailureMessage(Throwable throwable) {
    this.throwable = throwable;
  }

  public Throwable getThrowable() {
    return throwable;
  }
}
