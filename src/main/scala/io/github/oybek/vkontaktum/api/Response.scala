package io.github.oybek.vkontaktum.api

case class Response[T](count: Int, items: List[T])
