package me.leaf.www.actors

import akka.actor.Actor
import me.leaf.www.models.Resource
import org.joda.time.DateTime
import play.api.libs.json.Json
import scalaj.http.{Http, HttpResponse}

case class GetLinks(url: String)
case class Update(url: String, userIds: Seq[Int])

class ApiConsumer extends Actor{
  implicit val excon = context.dispatcher

  def receive = {
    case GetLinks(url) => getLinks(url)
    case Update(url, userIds) => update(url, userIds)
  }

  def getLinks(url: String) = {
    val result: HttpResponse[String] = Http(url).asString
    sender ! Json.parse(result.body).as[Seq[Resource]]
  }

  def update(url: String, userIds: Seq[Int]) = {
    println("UPDATING RESOURCE")
    val time = new DateTime
    userIds.map { userId =>
      Http(url).postData(
        s"""{"user_id": $userId, "last_fetch": "$time"}"""
      ).header("content-type", "application/json").asString
    }
  }

}
