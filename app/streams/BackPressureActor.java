package streams;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import play.Logger;
import streams.protocol.*;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class BackPressureActor extends AbstractActor {

  private static AtomicInteger num = new AtomicInteger();

  private int actornum;

  private static final ScheduledExecutorService scheduler =
      Executors.newScheduledThreadPool(
          1,
          new ThreadFactoryBuilder()
              .setDaemon(true)
              .setNameFormat("completeAfter-%d")
              .build());

  /**
   * Заполнение фьючерса по таймауту
   */
  static CompletableFuture<ProcessMessage> timeout(Duration duration, ProcessMessage processMessage) {
    CompletableFuture<ProcessMessage> timeoutCf = new CompletableFuture<>();
    scheduler.schedule(() -> {
      timeoutCf.complete(processMessage);
    }, duration.toMillis(), MILLISECONDS);
    return timeoutCf;
  }

  public BackPressureActor(){
    this.actornum = num.incrementAndGet(); 
  }

  static public Props props() {
    return Props.create(BackPressureActor.class, BackPressureActor::new);
  }
  @Override
  public Receive createReceive() {
    return receiveBuilder().
        match(InitMessage.class, msg -> {
          Logger.info("Receive init message in " + self().path().toSerializationFormat());
          sender().tell(AckMessage.getInstanse(), self());
        })
        .match(ProcessMessage.class, msg -> {
          Logger.info(String.format("Receive process message :%s in %s", msg.getMessage(), self().path().toSerializationFormat()));
          ActorRef sender = getSender();
          timeout(Duration.ofSeconds(5 + (long)(Math.random()* 10)), msg)
              .whenComplete( (done, thr)-> {
                Logger.info(String.format("Complete message %s, sending ackMessage from %s", done.getMessage(), self().path().toSerializationFormat()));
                sender.tell(AckMessage.getInstanse(), self());
              });
        })
        .match(CompleteMessage.class, msg -> {
          Logger.info("Receive complete message in " + self().path().toSerializationFormat());
        })
        .match(FailureMessage.class, msg -> {
          Logger.info("Receive failure message in " + self().path().toSerializationFormat());
        })
        .build();
  }
}
