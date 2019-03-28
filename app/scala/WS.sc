/*

*/
import java.io.File
import java.util.function.BiConsumer

import akka.japi.Predicate
import org.eclipse.jgit.lib._
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.lib.Repository
import org.scalawag.sbt.gitflow._

import scala.collection.JavaConversions._
import org.eclipse.jgit.revwalk._
import java.util.function.{BiPredicate, Function => JFunction, Predicate => JPredicate}

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.{CredentialsProvider, UsernamePasswordCredentialsProvider}
object WS {
  var filedir = new File("/Users/serge/work/testPlay");
  var repo = (new FileRepositoryBuilder).findGitDir(filedir).build;
  println(repo.getBranch)
  repo.getAllRefs.forEach(new BiConsumer[String, Ref] {
    override def accept(t: String, u: Ref) = {
      println(t);
    }
  })
  var gitFlow = new GitFlow(repo);
  println("Check release branch: " + checkReleaseBranch(repo))
  println("CurrentVersion: " + gitFlow.version)
  if(checkReleaseBranch(repo)){
    throw new IllegalArgumentException("release branch exists");
  }

  var refWalk = new RevWalk(repo);
  var git = new Git(repo);
  //1. Делаем чекаут в дев
  git.pull().setRemoteBranchName("refs/remotes/origin/develop")
  var checkout = git.checkout().setName("develop").call()
  //2. Вычисляем версию нашего релиза
  var version = gitFlow.version.toString.split("-").toList.head;
  println(version)
  var releaseBranchName = "release/" + version;
  val releaseBranch = git.branchCreate().setName(releaseBranchName).call();
  git.push().add(releaseBranch)
    .setCredentialsProvider(new UsernamePasswordCredentialsProvider("Makaruch","Makar049")).call();
  def checkReleaseBranch(repository: Repository) = {
    repository.getAllRefs.keySet().toList.exists(_.contains("release"))
  }
}