package io.github.oybek.vkontaktum.api

import cats.data.NonEmptyList
import cats.effect.Sync
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._
import io.circe.syntax._
import io.circe.{Encoder, Printer}
import org.http4s.multipart.{Multipart, Part}

case class SendMessageReq(peerId: Option[Long],
                          peerIds: Option[NonEmptyList[Long]],
                          message: String,
                          version: String,
                          randomId: Long,
                          accessToken: String,
                          userIds: Option[NonEmptyList[Long]] = None,
                          attachment: Option[String] = None,
                          keyboard: Option[Keyboard] = None)
    extends Req {
  def toRequestStr: String = ""

  def toMultipart[F[_]]: Multipart[F] =
    Multipart[F](
      Vector(
        "peer_id" -> peerId.map(_.toString),
        "peer_ids" -> peerIds.map(_.toList.mkString(",")),
        "access_token" -> Some(accessToken),
        "v" -> Some(version),
        "random_id" -> Some(randomId.toString),
        "user_ids" -> userIds.map(_.toList.mkString(",")),
        "message" -> Some(message),
        "attachment" -> attachment,
        "keyboard" -> keyboard.map(x =>
          Printer.noSpaces
            .copy(dropNullValues = true)
            .print(x.asJson)
        )
      ).collect { case (k, Some(v)) => k -> v }
        .map {
          case (k, v) => Part.formData[F](k, v)
        }
    )
}

case class Keyboard(oneTime: Option[Boolean] = None, inline: Boolean, buttons: List[List[Button]])
case class Button(action: Action, color: Option[String] = None)
case class Action(`type`: String,
                  link: Option[String] = None,
                  label: Option[String] = None,
                  payload: Option[String] = None,
                  hash: Option[String] = None)

object Keyboard {
  implicit val customConfig: Configuration =
    Configuration.default.withSnakeCaseMemberNames

  implicit val encodeKeyboard: Encoder[Keyboard] = deriveConfiguredEncoder
  implicit val encodeButton: Encoder[Button] = deriveConfiguredEncoder
  implicit val encodeAction: Encoder[Action] = deriveConfiguredEncoder
}

case class SendMessageRes(response: Option[Long])
