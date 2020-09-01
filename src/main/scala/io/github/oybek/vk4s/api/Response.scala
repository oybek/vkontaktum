package io.github.oybek.vk4s.api

case class Response[T](count: Int, items: List[T])
