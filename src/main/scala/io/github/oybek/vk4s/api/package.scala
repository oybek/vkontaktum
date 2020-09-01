package io.github.oybek.vk4s

import io.circe.generic.extras.Configuration

package object api {
  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames
}
