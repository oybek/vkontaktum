package io.github.oybek.vkontaktum

import io.circe.generic.extras.Configuration

package object api {
  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames
}
