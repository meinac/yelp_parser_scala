package me.leaf.www.models

import play.api.libs.json.Json

case class Rating(average: String)

object Rating {

  implicit val ratingFormat = Json.format[Rating]

}
