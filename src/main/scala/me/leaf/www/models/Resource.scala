package me.leaf.www.models

import org.joda.time.DateTime
import play.api.libs.json.{JsPath, Reads}
import play.api.libs.functional.syntax._
import me.leaf.www.json.JodaConverters._

case class Resource(userId: Int, url: String, lastFetch: Option[DateTime])

object Resource {

  implicit val resourceReader : Reads[Resource] = (
    (JsPath \ "user_id").read[Int] and
      (JsPath \ "url").read[String] and
      (JsPath \ "last_fetch").readNullable[DateTime]
    )(Resource.apply _)

}
