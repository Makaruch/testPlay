package streams.protocol;

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
