package me.leaf.www.actors
import akka.actor._
import akka.pattern._
import awscala._
import awscala.sqs._
import com.typesafe.config.ConfigFactory
import me.leaf.www.actors.Pusher.Push
import me.leaf.www.models.{Letter, Resource}
import scala.concurrent.duration._
import akka.util.Timeout

object Dispatcher {

  case object Dispatch

  class Dispatcher(apiConsumer: ActorRef, parser: ActorRef, pusher: ActorRef) extends Actor {

    implicit val excon = context.dispatcher
    implicit val timeOut = Timeout(10 minutes)

    lazy val config = ConfigFactory.load()
    val api = config.getString("api")
    val resourceUrl = s"""${api}/yelp/list"""
    val updateUrl = s"""${api}/yelp/update"""
    val awsId = config.getString("aws.id")
    val awsKey = config.getString("aws.key")
    val queueName = config.getString("queue")

    val credentials = Credentials(awsId, awsKey)
    implicit val region = Region.US_EAST_1
    val sqs = SQS(credentials)
    val queue = sqs.queue(queueName).getOrElse(throw new Exception("Queue not found."))

    def receive = {
      case Dispatch => dispatch
    }

    def dispatch = (for {
      resources <- getLinks
      parsed <- parse(resources)
      _ = push(parsed)
      _ = update(resources.map(_.userId))
    } yield context.system.scheduler.scheduleOnce(2 minutes, self, Dispatch))
    .recover{
      case e =>
        println(e.getMessage)
        context.system.scheduler.scheduleOnce(1 minutes, self, Dispatch)
    }


    def getLinks = (apiConsumer ? GetLinks(resourceUrl)).mapTo[Seq[Resource]]
    def parse(resources: Seq[Resource]) = (parser ? Parse(resources)).mapTo[Seq[Letter]]
    def push(letters: Seq[Letter]) = pusher ! Push(letters, queue, sqs)
    def update(userIds: Seq[Int]) = apiConsumer ! Update(updateUrl, userIds)

  }

}