package io.github.oybek.vk4s.domain

import io.circe._
import io.circe.parser._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EventParseSpec extends AnyFlatSpec with Matchers {

  val audioMessageEventRaw =
    """
      |{
      |  "type": "message_new",
      |  "object": {
      |    "message": {
      |      "date": 1599546317,
      |      "from_id": 213461412,
      |      "id": 9784,
      |      "out": 0,
      |      "peer_id": 213461412,
      |      "text": "",
      |      "conversation_message_id": 8266,
      |      "fwd_messages": [],
      |      "important": false,
      |      "random_id": 0,
      |      "attachments": [
      |        {
      |          "type": "audio_message",
      |          "audio_message": {
      |            "id": 567682037,
      |            "owner_id": 213461412,
      |            "duration": 2,
      |            "waveform": [ 1, 2, 3 ],
      |            "link_ogg": "https:\/\/psv4.userapi.com\/c619228\/\/u213461412\/audiomsg\/d1\/d9dfe160e4.ogg",
      |            "link_mp3": "https:\/\/psv4.userapi.com\/c619228\/\/u213461412\/audiomsg\/d1\/d9dfe160e4.mp3",
      |            "access_key": "c32bc578e803704830"
      |          }
      |        }
      |      ],
      |      "is_hidden": false
      |    }
      |  }
      |}
      |""".stripMargin

  "parse message_new event with audio_message" must "be parsed ok" in {
    decode[Event](audioMessageEventRaw) should be (Right(
      MessageNew(
        id = 9784,
        date = 1599546317,
        peerId = 213461412,
        fromId = 213461412,
        text = "",
        geo = None,
        attachments = List(
          AudioMessage(
            id = 567682037,
            ownerId = 213461412,
            duration = 2,
            linkMp3 = "https://psv4.userapi.com/c619228//u213461412/audiomsg/d1/d9dfe160e4.mp3",
            accessKey = "c32bc578e803704830"
          )
        )
      )
    ))
  }
}
