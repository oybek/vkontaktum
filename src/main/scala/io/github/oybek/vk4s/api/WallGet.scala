package io.github.oybek.vk4s.api

import io.github.oybek.vk4s.domain.WallPostNew

case class WallGetReq(ownerId: Long,
                      offset: Long,
                      count: Long,
                      version: String,
                      accessToken: String)
    extends Req {
  def toRequestStr: String =
    Seq(
      "owner_id" -> ownerId,
      "offset" -> offset,
      "count" -> count,
      "v" -> version,
      "access_token" -> accessToken
    ).map {
      case (k, v) => k + "=" + v
    }.mkString("&")
}

case class WallGetRes(response: Response[WallPostNew])
