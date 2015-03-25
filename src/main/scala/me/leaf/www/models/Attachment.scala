package me.leaf.www.models

import play.api.libs.json._

case class Attachment(image_url: String)

object Attachment {

  implicit val attachmentFormat = Json.format[Attachment]

}