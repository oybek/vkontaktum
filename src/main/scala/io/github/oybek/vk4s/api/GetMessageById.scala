package io.github.oybek.vk4s.api

import io.github.oybek.vk4s.domain.{MessageNew, WallPostNew}

case class GetMessageByIdReq(messageIds: List[Long],
                             version: String,
                             accessToken: String) extends Req {
  def toRequestStr: String =
    Seq(
      "v" -> version,
      "access_token" -> accessToken,
      "message_ids" -> messageIds.mkString(",")
    ).map {
      case (k, v) => k + "=" + v
    }.mkString("&")
}

case class GetMessageByIdRes(response: Response[MessageNew])
