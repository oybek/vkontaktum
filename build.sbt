
ThisBuild / version := "0.1"
ThisBuild / organization := "io.github.oybek"
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / scalacOptions ++= Seq(
  "-encoding", "utf8", // Option and arguments on same line
  "-Xfatal-warnings",  // New lines for each options
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)

lazy val vkontaktum = (project in file("."))
  .settings(name := "vkontaktum")
  .settings(libraryDependencies ++= Dependencies.common)
  .settings(sonarProperties := Sonar.properties)
