package me.leaf.www.models


import org.joda.time.DateTime
import play.api.libs.json._

sealed trait Gender
case object Male extends Gender
case object Female extends Gender

case class User(id: String, name: String, gender: Option[String], email: Option[String], birth_date: Option[DateTime],
                image_url: Option[String], places_lived: Option[Seq[String]], url: Option[String])

object User {
  implicit val userFormat = Json.format[User]
}
