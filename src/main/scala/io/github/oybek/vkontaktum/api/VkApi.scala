package io.github.oybek.vkontaktum.api

import io.github.oybek.vkontaktum.api.WallGetRes

trait VkApi[F[_]] {
  def getLongPollServer(
    getLongPollServerReq: GetLongPollServerReq
  ): F[GetLongPollServerRes]
  def poll(pollReq: PollReq): F[PollRes]

  def sendMessage(sendMessageReq: SendMessageReq): F[String]
  def getMessageById(getMessageByIdReq: GetMessageByIdReq): F[GetMessageByIdRes]
  def wallComment(wallCommentReq: WallCommentReq): F[WallCommentRes]
  def wallGet(wallGetReq: WallGetReq): F[WallGetRes]

  def getConversations(getConversationsReq: GetConversationsReq): F[GetConversationsRes]
}
