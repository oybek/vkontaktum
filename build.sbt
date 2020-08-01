
ThisBuild / version := "0.1"
ThisBuild / organization := "io.github.oybek"

lazy val vk4s = (project in file("."))
  .settings(name := "vk4s")
  .settings(libraryDependencies ++= Dependencies.common)
  .settings(sonarProperties := Sonar.properties)
  .settings(Compiler.settings)
