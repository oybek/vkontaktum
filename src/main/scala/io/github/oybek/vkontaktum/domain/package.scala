package io.github.oybek.vkontaktum

import io.circe.generic.extras.Configuration

package object domain {
  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames
}
