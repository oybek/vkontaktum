import sbt._

object Dependencies {

  object V {
    val catsCore = "2.1.1"
    val catsEffect = "2.1.4"
    val circe = "0.13.0"
    val scalaTest = "3.2.0"
    val http4s = "0.21.7"
    val slf4j = "1.7.26"
    val logback = "1.2.3"
    val mock = "4.4.0"
    val mockTest = "3.1.0"
  }

  val catsCore = "org.typelevel" %% "cats-core" % V.catsCore
  val catsEffect = "org.typelevel" %% "cats-effect" % V.catsEffect
  val scalaTest = "org.scalatest" %% "scalatest" % V.scalaTest % Test

  val circe = Seq(
    "io.circe" %% "circe-core" % V.circe,
    "io.circe" %% "circe-parser" % V.circe,
    "io.circe" %% "circe-generic" % V.circe,
    "io.circe" %% "circe-generic-extras" % V.circe
  )

  val http4s = Seq(
    "org.http4s" %% "http4s-dsl" % V.http4s,
    "org.http4s" %% "http4s-circe" % V.http4s,
    "org.http4s" %% "http4s-blaze-client" % V.http4s
  )

  val logger = Seq(
    "org.slf4j" % "slf4j-api" % V.slf4j,
    "ch.qos.logback" % "logback-classic" % V.logback
  )

  val mock = Seq(
    "org.scalamock" %% "scalamock" % V.mock % Test,
    "org.scalatest" %% "scalatest" % V.mockTest % Test
  )

  val testContainers = Seq(
    "com.dimafeng" %% "testcontainers-scala-core" % "0.37.0" % "test",
    "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.37.0" % "test",
    "com.dimafeng" %% "testcontainers-scala-postgresql" % "0.37.0" % "test"
  )

  val common = Seq(catsCore, catsEffect, scalaTest) ++
    circe ++
    http4s ++
    logger ++
    mock ++
    testContainers
}
