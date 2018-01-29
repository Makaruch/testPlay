package streams;

import akka.actor.AbstractActor;
import akka.actor.PoisonPill;
import akka.actor.Props;
import play.Logger;
import scala.concurrent.duration.Duration;
import streams.protocol.ProcessMessage;
import streams.protocol.WorkFinishedMessage;

import java.util.concurrent.TimeUnit;

/**
 * Актор-работник. Выполняет задачу и отправляет ответ по завершению.
 */
public class WorkerActor extends AbstractActor {

 public static Props props(){
   return Props.create(WorkerActor.class, WorkerActor::new);
 }

  @Override
  public Receive createReceive() {
    return receiveBuilder().match(ProcessMessage.class, msg -> {
      if(Math.random() > 0.9){
        Logger.info("Message " + msg.getMessage() + " dropped");
        self().tell(PoisonPill.getInstance(), self());
        return;
      }
      context().system().scheduler().scheduleOnce(Duration.apply(5 + (long)(Math.random() * 10), TimeUnit.SECONDS), sender(), new WorkFinishedMessage(msg.getId()), context().system().dispatcher(), self());
      self().tell(PoisonPill.getInstance(), self());
    }).build();
  }
}
