name := "play-scala-intro"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(jdbc,  "com.typesafe.play" %% "anorm" % "2.5.1")
libraryDependencies += evolutions

