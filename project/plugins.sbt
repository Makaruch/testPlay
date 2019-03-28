logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

//addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.9")
addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "5.0.0")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.11")
addSbtPlugin("ru.sshagin" % "sbt-gitflow" % "0.2-SNAPSHOT")
