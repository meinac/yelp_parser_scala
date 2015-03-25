package me.leaf.www.models

import org.joda.time.DateTime
import play.api.libs.json._
import me.leaf.www.json.JodaConverters
import scala.collection.JavaConversions._

case class Letter(provider: String,
                  letter_type: String,
                  user_id: Int,   //user id in leaf
                  known_id: String,
                  content: String,
                  rating: String,
                  user: User,
                  attachment: Option[Seq[Attachment]],
                  created: DateTime,
                  updated: Option[DateTime])

object Letter {

  implicit val letterFormat = Json.format[Letter]

}