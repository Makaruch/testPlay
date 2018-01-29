package streams.protocol;

/**
 * Сообщение о выполнении задачи. Отправляется работником
 */
public class WorkFinishedMessage {
  private final String id;

  public WorkFinishedMessage(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
