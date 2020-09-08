# Vk4s ![master](https://github.com/oybek/vk4s/workflows/master/badge.svg) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=oybek_vk4s&metric=ncloc)](https://sonarcloud.io/dashboard?id=oybek_vk4s) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=oybek_vk4s&metric=coverage)](https://sonarcloud.io/dashboard?id=oybek_vk4s) <a href="https://typelevel.org/cats/"><img src="https://typelevel.org/cats/img/cats-badge.svg" height="40px" align="right" alt="Cats friendly" /></a>

Pure functional library for working with vk bot api

```scala
class VkBot[F[_]: Async: Timer: Concurrent](getLongPollServerReq: GetLongPollServerReq)
                                           (implicit httpClient: Client[F], vkApi: VkApi[F])
    extends LongPollBot[F](httpClient, vkApi, getLongPollServerReq) {

  implicit val log: Logger = LoggerFactory.getLogger("VkGate")

  override def onMessageNew(message: MessageNew): F[Unit] = message match {
    case MessageNew(_, _, peerId, _, _, Some(Geo(coord, _)), _) =>
      vkApi.sendMessage(
        SendMessageReq(
          peerId = peerId,
          message = $"you've send geo (${coord.latitude}, ${coord.longitude})",
          version = getLongPollServerReq.version,
          randomId = 4,
          accessToken = getLongPollServerReq.accessToken
        )
      )

    case MessageNew(_, _, peerId, _, text, _, _) =>
      sendMessage(peerId, text)
  }

  override def onWallPostNew(wallPostNew: WallPostNew): F[Unit] = ...

  override def onWallReplyNew(wallReplyNew: WallReplyNew): F[Unit] = ...
```
