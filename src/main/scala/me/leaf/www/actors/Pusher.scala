package me.leaf.www.actors

import akka.actor.{Actor}
import awscala._, sqs._
import me.leaf.www.models.Letter
import play.api.libs.json.Json

object Pusher {

  case class Push(letters: Seq[Letter], queue: Queue, sqs: SQS)

  class Pusher extends Actor {

    def receive = {
      case pushObject: Push => push(pushObject.letters, pushObject.queue, pushObject.sqs)
    }

    def push(letters: Seq[Letter], queue: Queue, sqs: SQS) = {
      println("PUSHING " + letters.length + " letters")
      implicit val sQS = sqs
      letters.foreach{ letter =>
        queue.add(Json.toJson(letter).toString())
      }
    }

  }

}
