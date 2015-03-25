package me.leaf.www.actors

import java.text.{SimpleDateFormat, DateFormat}
import java.util.Locale

import akka.actor.Actor
import me.leaf.www.models.{Attachment, User, Letter, Resource}
import org.joda.time.DateTime
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.concurrent.Future

case class Parse(resources: Seq[Resource])

class Parser extends Actor {
  val timeOut = 5000;
  val dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH)

  def receive = {
    case parsable: Parse => parse(parsable.resources)
  }

  def parse(resources: Seq[Resource]) = {
    val letters = resources.flatMap { resource =>
      val document = Jsoup.connect(resource.url).timeout(timeOut).get;
      parsePage(document, resource.userId).filter(letter => letter.created.isAfter(resource.lastFetch.getOrElse(new DateTime(0))))
    }
    sender ! letters
  }

  def parsePage(document: Document, userId: Int): Seq[Letter] = {
    val reviews = document.select(".reviews")
    val letters = reviews.select("div.review").toArray.map{ review =>
      parseReview(review.toString, userId)
    }

    val nextButton = document.select("a.next")
    if(!nextButton.isEmpty) {
      return letters ++ parsePage(Jsoup.connect(nextButton.attr("href")).timeout(timeOut).get, userId)
    } else {
      return letters
    }
  }

  def parseReview(content: String, userId: Int) = {
    val review = Jsoup.parse(content)
    val date = dateFormat.parse(review.select(".rating-qualifier").select("meta").attr("content"))
    val rating = review.select(".rating-very-large").select("meta").attr("content")
    val comment = review.select(".review-content p").text()
    val user = parseUser(review)
    val attachments = parseAttachments(review)
    Letter(
      "yelp",
      "yelp_comment",
      userId,
      "",
      comment,
      rating,
      user,
      Some(attachments),
      new DateTime(date.getTime),
      None
    )
  }

  def parseUser(review: Document) = {
    val userName = review.select(".user-display-name").text()
    val userFrom = review.select(".user-location b").text()
    val profileLink = review.select(".user-display-name").attr("href")
    val profileId = profileLink.split("=")(1)
    val profilePic= review.select(".media-avatar .photo-box-img").attr("src")
    User(
      profileId,
      userName,
      None, None, None,
      Some(profilePic),
      Some(Seq(userFrom)),
      Some(profileLink)
    )
  }

  def parseAttachments(review: Document) = {
    review.select(".photo-box-grid li").toArray.map{ imgNode =>
      Attachment(
        "",
        Jsoup.parse(imgNode.toString).select("img").attr("src")
      )
    }.toSeq
  }

}