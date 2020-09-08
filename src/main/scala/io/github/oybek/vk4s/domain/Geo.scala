package io.github.oybek.vk4s.domain

import io.circe.generic.extras.auto._
import io.circe.{Decoder, HCursor}

final case class Geo(coordinates: Coord, place: Option[Place])

object Geo {
  implicit val decodeGeo: Decoder[Geo] =
    (c: HCursor) =>
      for {
        coordEither <- c.downField("coordinates").as[Either[String, Coord]]
        coord = coordEither match {
          case Left(s) =>
            val coords = s.split(' ')
            Coord(coords(0).toFloat, coords(1).toFloat)
          case Right(coord) => coord
        }
        place <- c.downField("place").as[Option[Place]]
        res = Geo(coord, place)
      } yield res

  implicit def decodeEither[A, B](implicit a: Decoder[A],
                                  b: Decoder[B]): Decoder[Either[A, B]] = {
    val l: Decoder[Either[A, B]] = a.map(Left.apply)
    val r: Decoder[Either[A, B]] = b.map(Right.apply)
    l or r
  }
}
