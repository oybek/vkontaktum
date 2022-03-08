package io.github.oybek.vkontaktum.domain

case class Peer(id: Int, `type`: String, localId: Int)
case class Conversation(peer: Peer, unanswered: Boolean)

case class ConversationAndLastMessage(conversation: Conversation)
