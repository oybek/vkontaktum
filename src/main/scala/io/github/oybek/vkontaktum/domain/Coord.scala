package io.github.oybek.vkontaktum.domain

final case class Coord(latitude: Float, longitude: Float) {
  def sq(x: Float) = x * x
  def distSq(c: Coord): Float =
    sq(c.latitude - latitude) + sq(c.longitude - longitude)
}
