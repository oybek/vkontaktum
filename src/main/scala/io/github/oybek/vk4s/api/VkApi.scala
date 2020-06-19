package io.github.oybek.vk4s.api

import io.github.oybek.vk4s.model.{GetLongPollServerReq, GetLongPollServerRes, PollReq, PollRes, SendMessageReq, WallCommentReq, WallCommentRes, WallGetReq, WallGetRes}

trait VkApi[F[_]] {
  def getLongPollServer(
    getLongPollServerReq: GetLongPollServerReq
  ): F[GetLongPollServerRes]
  def poll(pollReq: PollReq): F[PollRes]

  def sendMessage(sendMessageReq: SendMessageReq): F[String]
  def wallComment(wallCommentReq: WallCommentReq): F[WallCommentRes]
  def wallGet(wallGetReq: WallGetReq): F[WallGetRes]
}
