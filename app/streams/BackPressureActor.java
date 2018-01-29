package streams;

import akka.actor.*;
import play.Logger;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import streams.protocol.*;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BackPressureActor extends AbstractActor {


  private static int PARALLELIZM = 10;

  private static Map<String, Work> works = new ConcurrentHashMap<>();


  static public Props props() {
    return Props.create(BackPressureActor.class, BackPressureActor::new);
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder().
        match(InitMessage.class, msg -> {
          //Первое сообщение в последовательности, отвечаем отправляющему, что готовы работать
          Logger.info("Receive init message in " + self().path().toSerializationFormat());
          sender().tell(AckMessage.getInstanse(), self());
        })
        .match(CompleteMessage.class, msg -> {
          //Последнее сообщение в последовательности, которое говорит о том, что задачи кончились
          Logger.info("Receive complete message in " + self().path().toSerializationFormat());
          //do nothing
        })
        .match(ProcessMessage.class, msg -> {
          //Сообщение с задачей, которую надо выполнить
          Logger.info(String.format("Receive process message :%s in %s", msg.getMessage(), self().path().toSerializationFormat()));
          //Создаем работника
          ActorRef worker = context().actorOf(WorkerActor.props());
          //Отправляем работнику сообщение с задачей
          worker.tell(msg, self());
          // Делаем напоминалку себе, что если задача долго не выполняется, то скорее всего она зависла и надо запросить еще
          works.put(msg.getId(), new Work(sender(), workTimeout(Duration.apply(20, SECONDS), msg.getId())));
          //Если надо запросить еще задачку - запрашиваем
          if (works.size() < PARALLELIZM) {
            sender().tell(AckMessage.getInstanse(), self());
          }
        })
        .match(WorkFinishedMessage.class, msg -> {
          //Сообщение от работника, что задача выполнена
          Logger.info("Receive work finished message in " + self().path().toSerializationFormat());
          //Ищем задачу у себя и отправляем поставщику сообщение с запросом на новую
          Optional.ofNullable(works.get(msg.getId())).ifPresent(work -> {
            work.getTimeout().cancel();
            works.remove(msg.getId());
            if (works.size() < PARALLELIZM) {
              work.getSupplier().tell(AckMessage.getInstanse(), self());
            }
          });
        })
        .match(WorkTimeout.class, msg -> {
          //Сообщение о том, что какая-то задача слишком долго выполняется
          Logger.info("Receive workTimeout message in " + self().path().toSerializationFormat());
          //Ищем задачу у себя и отправляем поставщику сообщение с запросом на новую
          Optional.ofNullable(works.get(msg.getId())).ifPresent(work -> {
            works.remove(msg.getId());
            if (works.size() < PARALLELIZM) {
              work.getSupplier().tell(AckMessage.getInstanse(), self());
            }
          });
        })
        .match(FailureMessage.class, msg -> {
          Logger.info("Receive failure message in " + self().path().toSerializationFormat());
        })
        .build();
  }

  /**
   * Метод делает напоминалку о зависшей задаче
   * @param duration через сколько напомнить
   * @param workId что за задача
   */
  private Cancellable workTimeout(FiniteDuration duration, String workId) {
    return context().system().scheduler().scheduleOnce(duration, self(), new WorkTimeout(workId), context().system().dispatcher(), ActorRef.noSender());
  }

  /**
   * Класс задачки для внутреннего контроля
   */
  private class Work {
    //Ссылка на поставщика
    private final ActorRef supplier;
    //Напоминалка, которую можно отменить
    private final Cancellable timeout;

    public Work(ActorRef supplier, Cancellable timeout) {
      this.supplier = supplier;
      this.timeout = timeout;
    }

    public ActorRef getSupplier() {
      return supplier;
    }

    public Cancellable getTimeout() {
      return timeout;
    }
  }
}
