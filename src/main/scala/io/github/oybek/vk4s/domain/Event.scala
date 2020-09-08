package io.github.oybek.vk4s.domain

import io.circe.{Decoder, DecodingFailure, HCursor}
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.auto._
import org.http4s.DecodeFailure

sealed trait Event

final case class MessageNew(id: Long,
                            date: Long,
                            peerId: Long,
                            fromId: Long,
                            text: String,
                            geo: Option[Geo],
                            attachments: List[Attachment]) extends Event

final case class WallPostNew(id: Long,
                             fromId: Long,
                             ownerId: Long,
                             date: Long,
                             markedAsAds: Option[Long],
                             text: String,
                             signerId: Option[Long],
                             postType: Option[String],
                             geo: Option[Geo]) extends Event

case class WallReplyNew(id: Long,
                        fromId: Long,
                        text: String,
                        postId: Long,
                        date: Long)
    extends Event

object Event {
  implicit val decodeEvent: Decoder[Event] =
    (c: HCursor) =>
      for {
        typee <- c.downField("type").as[String]
        res <- typee match {
          case "message_new" =>
            c.downField("object").downField("message").as[MessageNew]
          case "wall_post_new"  => c.downField("object").as[WallPostNew]
          case "wall_reply_new" => c.downField("object").as[WallReplyNew]
          case eventType =>
            Left(DecodingFailure(s"Unknown event type: $eventType", List()))
        }
      } yield res
}
