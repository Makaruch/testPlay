import sbtrelease.Version.Bump.Micro

name := "testPlay"

//version := "1.0.1-SNAPSHOT"

lazy val testPlay = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
lazy val akkaVersion = "2.5.3"

//resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalaVersion := "2.12.8"

//releaseUseGlobalVersion := fваваalse
//releaseIgnoreUntrackedFiles := true
libraryDependencies ++= Seq(javaJdbc, javaWs, guice)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "org.postgresql" % "postgresql" % "42.0.0",
  "com.typesafe.akka" %% "akka-stream" % akkaVersion)

releaseVersionBump := Micro

//unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

//publishTo := Some(Resolver.file("file",  new File( "C:\\Users\\Serge\\maven-releases" )) )

