logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.9")
addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "4.0.1")
addSbtPlugin("com.servicerocket" % "sbt-git-flow" % "0.1.2")

libraryDependencies += "org.scalawag.sbt.gitflow" % "sbt-gitflow" % "1.1.0"
