package streams.protocol;

/**
 * Сообщение напоминалки о том, что задача долго висит. Отправляется BackPressureActor самому себе
 */
public class WorkTimeout {
  private final String id;

  public WorkTimeout(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
