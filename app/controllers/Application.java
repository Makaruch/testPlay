package controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.Broadcast;
import akka.routing.RoundRobinPool;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import io.ebean.Ebean;
import io.ebean.Transaction;
import play.Logger;
import play.mvc.*;

import streams.BackPressureActor;
import streams.protocol.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Application extends Controller {

    @Inject
    Materializer materializer;

    @Inject
    ActorSystem actorSystem;


    String q1 = "INSERT INTO temp_table (buyer_id, duration, invoice_id) VALUES (1,1,1);";
    String q2 = "INSERT INTO temp_table (buyer_id, duration, invoice_id) VALUES (2,2,2);";
    String truncate = "truncate temp_table cascade;";


    public Result index() {
      try(Transaction tr = Ebean.beginTransaction()){
        Ebean.createSqlUpdate(q1).execute();
        test();
        if(1==1){
          throw new RuntimeException();
        }
        tr.commit();
      }
      return ok("ok");
    }

    void test(){
      Transaction tr = Ebean.beginTransaction();
      Ebean.createSqlUpdate(q2).execute();
      tr.commitAndContinue();
    }

}
