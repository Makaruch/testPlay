package streams.protocol;

import play.api.libs.Codecs;

/**
 * Сообщение с задачей. Отправляется поставщиком
 */
public class ProcessMessage {
  private final String message;

  public ProcessMessage(String message) {
    this.message = message;
    }

  public String getMessage() {
    return message;
  }

  public String getId(){
    return Codecs.sha1(message);
  }
}
