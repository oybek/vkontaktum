package io.github.oybek.vk4s.domain

import io.circe.{Decoder, DecodingFailure, HCursor}
import io.circe.generic.extras.auto._

sealed trait Attachment
case class AudioMessage(id: Long,
                        ownerId: Long,
                        duration: Int,
                        linkMp3: String,
                        accessKey: String) extends Attachment

object Attachment {
  implicit val decodeAttachment: Decoder[Attachment] =
    (c: HCursor) =>
      for {
        typee <- c.downField("type").as[String]
        res <- typee match {
          case "audio_message" => c.downField("audio_message").as[AudioMessage]
          case attachmentType  => Left(DecodingFailure(s"Unknown event type: $attachmentType", List()))
        }
      } yield res
}
