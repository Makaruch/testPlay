package controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import play.mvc.*;

import streams.BackPressureActor;
import streams.protocol.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class Application extends Controller {

    @Inject
    Materializer materializer;

    @Inject
    ActorSystem actorSystem;

    public Result index() {
        ActorRef actor = actorSystem.actorOf(BackPressureActor.props(), "actor");

        List<String> lst = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            lst.add(String.format("Message %d", i));
        }

        Source.from(lst)
            .map(ProcessMessage::new)
            .runWith(Sink.actorRefWithAck(actor, new InitMessage(), AckMessage.getInstanse(), new CompleteMessage(), FailureMessage::new), materializer);

        return ok("ok");
    }

}
