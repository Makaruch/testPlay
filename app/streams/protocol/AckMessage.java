package streams.protocol;

/**
 * Класс с сообщением готовности обработать задачу (для реализации backpressure)
 */
public class AckMessage {
  private AckMessage(){};
  private static AckMessage ackMessage;
  public static AckMessage getInstanse(){
    if(ackMessage == null){
      ackMessage = new AckMessage();
    }
    return ackMessage;
  }
}
