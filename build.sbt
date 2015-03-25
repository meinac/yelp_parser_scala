name := "yelp_parser"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.7.2",
  "com.github.seratch" %% "awscala" % "0.5.+",
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "com.typesafe" % "config" % "1.2.1",
  "org.scalaj" %% "scalaj-http" % "1.1.4",
  "com.typesafe.play" % "play-json_2.11" % "2.4.0-M3"
)
    