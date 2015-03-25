package me.leaf.www

import actors.Dispatcher._
import akka.actor.{Props, ActorSystem}
import me.leaf.www.actors.Pusher.Pusher
import me.leaf.www.actors.{ApiConsumer, Parser}

object Main {

  def main(args: Array[String]) = {
    val actorSystem = ActorSystem("yelpParser")
    val apiConsumer = actorSystem.actorOf(Props[ApiConsumer], "API_CONSUMER")
    val parser = actorSystem.actorOf(Props[Parser], "PARSER")
    val pusher = actorSystem.actorOf(Props[Pusher], "PUSHER")
    val dispatcher = actorSystem.actorOf(Props(classOf[Dispatcher], apiConsumer, parser, pusher))
    dispatcher ! Dispatch
  }

}