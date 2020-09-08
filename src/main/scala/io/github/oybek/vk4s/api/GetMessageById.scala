package io.github.oybek.vk4s.api

import io.github.oybek.vk4s.domain.{MessageNew, WallPostNew}

case class GetMessageByIdReq(messageIds: List[Long]) extends Req {
  def toRequestStr: String =
    "message_ids=" + messageIds.mkString(",")
}

case class GetMessageByIdRes(response: Response[MessageNew])
