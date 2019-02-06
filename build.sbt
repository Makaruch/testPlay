name := "streamtest"
 
version := "1.0.1-SNAPSHOT"
      
lazy val `streamtest` = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
lazy val akkaVersion = "2.5.3"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
scalaVersion := "2.11.11"

libraryDependencies ++= Seq( javaJdbc , javaWs, guice )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "org.postgresql" % "postgresql" % "42.0.0",
  "com.typesafe.akka" %% "akka-stream" % akkaVersion)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

      