name := "DramaOnTheJVM"

version := "0.1"

scalaVersion := "2.12.8"

val akkaVersion = "2.5.19"
val scalaTestVersion = "3.0.5"
val zioVersion = "0.5.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "org.scalaz" %% "scalaz-zio" % zioVersion
)