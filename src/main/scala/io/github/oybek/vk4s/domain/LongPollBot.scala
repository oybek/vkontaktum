package io.github.oybek.vk4s.domain

import cats.implicits._
import cats.effect.Sync
import io.github.oybek.vk4s.api.VkApi
import io.github.oybek.vk4s.model.{GetLongPollServerReq, GetLongPollServerRes, PollFailed, PollReq, PollWithUpdates}
import org.http4s.client.Client
import org.slf4j.{Logger, LoggerFactory}

abstract class LongPollBot[F[_]: Sync](
  httpClient: Client[F],
  vkApi: VkApi[F],
  getLongPollServerReq: GetLongPollServerReq
) {

  implicit def log: Logger

  final private val getLongPollServer: F[GetLongPollServerRes] =
    for {
      attemptRes <- vkApi.getLongPollServer(getLongPollServerReq).attempt
      res <- attemptRes match {
        case Left(e: Exception) =>
          Sync[F].delay { log.warn(s"something went wrong: ${e.getMessage}") } *> getLongPollServer
        case Left(e) =>
          Sync[F].delay { log.warn(s"something went too wrong: ${e.getMessage}") } *> getLongPollServer
        case Right(x) => x.pure[F]
      }
    } yield res

  final def poll(pollReq: PollReq): F[Unit] =
    for {
      pollRes <- vkApi.poll(pollReq).attempt
      _ <- Sync[F].delay { log.info(s"poll result: $pollRes") }
      _ <- pollRes match {
        case Right(PollWithUpdates(ts, updates)) =>
          for {
            _ <- updates.traverse(onEvent)
            _ <- poll(pollReq.copy(ts = ts))
          } yield ()

        case Right(PollFailed(_, _)) =>
          start

        case Left(e: Exception) =>
          for {
            _ <- Sync[F].delay {
              log.warn(s"something went wrong: ${e.getMessage}")
            }
            _ <- start
          } yield ()

        case Left(e) =>
          for {
            _ <- Sync[F].delay {
              log.error(s"something went too wrong: ${e.getMessage}")
            }
            _ <- start
          } yield ()
      }
    } yield ()

  final val start: F[Unit] =
    for {
      getLongPollServerRes <- getLongPollServer
      longPollServer = getLongPollServerRes.response
      pollReq = PollReq(
        server = longPollServer.server,
        key = longPollServer.key,
        ts = longPollServer.ts,
        waitt = 20
      )
      _ <- poll(pollReq)
    } yield ()

  private def preHandleText(text: String): String =
    text.take(40).toLowerCase.replaceAll("\\[.*\\]", "").trim

  final def onEvent(event: Event): F[Unit] = event match {
    case messageNew: MessageNew =>
      onMessageNew(messageNew.copy(text = preHandleText(messageNew.text)))
    case wallPostNew: WallPostNew   => onWallPostNew(wallPostNew)
    case wallReplyNew: WallReplyNew => onWallReplyNew(wallReplyNew)
  }

  def onMessageNew(message: MessageNew): F[Unit]
  def onWallPostNew(wallPostNew: WallPostNew): F[Unit]
  def onWallReplyNew(wallReplyNew: WallReplyNew): F[Unit]
}
