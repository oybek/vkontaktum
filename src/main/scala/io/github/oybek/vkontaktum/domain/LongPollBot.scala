package io.github.oybek.vkontaktum.domain

import cats.effect.{Async, Sync}
import cats.implicits._
import io.github.oybek.vkontaktum.api.{GetLongPollServerReq, GetLongPollServerRes, PollFailed, PollReq, PollWithUpdates, VkApi}
import org.http4s.client.Client
import org.slf4j.Logger

import scala.concurrent.duration.DurationInt

abstract class LongPollBot[F[_]: Async](
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
          for {
            _ <- Async[F].delay { log.warn(s"something went wrong: ${e.getMessage}") }
            _ <- Async[F].sleep(10.seconds)
            server <- getLongPollServer
          } yield server
        case Left(e) =>
          for {
            _ <- Async[F].delay { log.warn(s"something went too wrong: ${e.getMessage}") }
            _ <- Async[F].sleep(100.seconds)
            server <- getLongPollServer
          } yield server
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
            _ <- Async[F].sleep(10.seconds)
            _ <- start
          } yield ()

        case Left(e) =>
          for {
            _ <- Sync[F].delay {
              log.error(s"something went too wrong: ${e.getMessage}")
            }
            _ <- Async[F].sleep(100.seconds)
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
