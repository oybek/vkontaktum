package io.github.oybek.vkontaktum.api

import io.github.oybek.vkontaktum.domain.ConversationAndLastMessage

sealed trait Filter
case object All extends Filter
case object Unread extends Filter
case object Important extends Filter
case object Unanswered extends Filter

case class GetConversationsReq(filter: Filter,
                               offset: Int,
                               count: Int,
                               version: String,
                               accessToken: String) extends Req {
  override def toRequestStr: String =
    Seq(
      "filter" -> filter.toString.toLowerCase,
      "offset" -> offset,
      "count" -> count,
      "v" -> version,
      "access_token" -> accessToken
    ).map {
      case (k, v) => k + "=" + v
    }.mkString("&")
}

case class GetConversationsRes(response: Response[ConversationAndLastMessage])
