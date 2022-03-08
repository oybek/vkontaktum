package io.github.oybek.vkontaktum.api

import cats.effect.Concurrent
import cats.syntax.all._
import io.circe.generic.extras.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.dsl.io._

class VkApiHttp4s[F[_]: Concurrent](client: Client[F]) extends VkApi[F] {

  private lazy val baseUrl = "https://api.vk.com"
  private lazy val methodUrl = baseUrl + "/method"

  override def getLongPollServer(
    getLongPollServerReq: GetLongPollServerReq
  ): F[GetLongPollServerRes] = {
    for {
      uri <- Concurrent[F].fromEither[Uri](
        Uri.fromString(
          s"$methodUrl/groups.getLongPollServer?${getLongPollServerReq.toRequestStr}"
        )
      )
      req = Request[F]().withMethod(GET).withUri(uri)
      res <- client.expect(req)(jsonOf[F, GetLongPollServerRes])
    } yield res
  }

  def poll(pollReq: PollReq): F[PollRes] = {
    for {
      uri <- Concurrent[F].fromEither[Uri](Uri.fromString(s"${pollReq.toRequestStr}"))
      req = Request[F]().withMethod(GET).withUri(uri)
      res <- client.expect(req)(jsonOf[F, PollRes])
    } yield res
  }

  override def sendMessage(sendMessageReq: SendMessageReq): F[String] = {
    for {
      uri <- Concurrent[F].fromEither[Uri](Uri.fromString(s"$methodUrl/messages.send"))
      entity = sendMessageReq.toMultipart[F]
      req = Request[F]()
        .withMethod(POST)
        .withUri(uri)
        .withEntity(entity)
        .withHeaders(entity.headers)
      res <- client.expect[String](req)
    } yield res
  }

  override def getMessageById(getMessageByIdReq: GetMessageByIdReq): F[GetMessageByIdRes] =
    for {
      uri <- Concurrent[F].fromEither[Uri](
        Uri.fromString(s"$methodUrl/messages.getById?${getMessageByIdReq.toRequestStr}")
      )
      req = Request[F]()
        .withMethod(POST)
        .withUri(uri)
      res <- client.expect(req)(jsonOf[F, GetMessageByIdRes])
    } yield res

  override def wallComment(wallCommentReq: WallCommentReq): F[WallCommentRes] =
    for {
      uri <- Concurrent[F].fromEither[Uri](
        Uri.fromString(
          s"$methodUrl/wall.createComment?${wallCommentReq.toRequestStr}"
        )
      )
      req = Request[F]()
        .withMethod(POST)
        .withUri(uri)
      res <- client.expect(req)(jsonOf[F, WallCommentRes])
    } yield res

  override def wallGet(wallGetReq: WallGetReq): F[WallGetRes] =
    for {
      uri <- Concurrent[F].fromEither[Uri](
        Uri.fromString(s"$methodUrl/wall.get?${wallGetReq.toRequestStr}")
      )
      req = Request[F]()
        .withMethod(POST)
        .withUri(uri)
      res <- client.expect(req)(jsonOf[F, WallGetRes])
    } yield res

  override def getConversations(getConversationsReq: GetConversationsReq): F[GetConversationsRes] =
    for {
      uri <- Concurrent[F].fromEither[Uri](
        Uri.fromString(s"$methodUrl/messages.getConversations?${getConversationsReq.toRequestStr}")
      )
      req = Request[F]()
        .withMethod(POST)
        .withUri(uri)
      res <- client.expect(req)(jsonOf[F, GetConversationsRes])
    } yield res
}
