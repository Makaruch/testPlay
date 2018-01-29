package controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.Broadcast;
import akka.routing.RoundRobinPool;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import play.mvc.*;

import streams.BackPressureActor;
import streams.protocol.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Application extends Controller {

    @Inject
    Materializer materializer;

    @Inject
    ActorSystem actorSystem;

    public Result index() {

        ActorRef actorPool = actorSystem.actorOf(new RoundRobinPool(2).props(BackPressureActor.props()), "actor" + Math.random());

        List<String> lst = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            lst.add(String.format("Message %d", i));
        }

        Source.from(lst)
            .map(ProcessMessage::new)
            .runWith(Sink.actorRefWithAck(actorPool, new Broadcast(new InitMessage()), AckMessage.getInstanse(), new Broadcast(new CompleteMessage()), FailureMessage::new), materializer);

        return ok("ok");
    }

}
