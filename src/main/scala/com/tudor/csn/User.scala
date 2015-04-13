package com.tudor.csn

import java.util
import java.util.Comparator
import java.util.concurrent.atomic.AtomicLong

import akka.actor.{Actor, ActorLogging, Props}
import com.tudor.csn.Commands.{Follow, ListMessages, Post, Wall}
import com.tudor.csn.User.Posts

class User(name: String) extends Actor with ActorLogging {

  import collection.JavaConverters._

  var idxSeq = new AtomicLong

  var storage = new util.TreeMap[(Long, Long), Post](User.TimestampOrdering)

  def messages = storage.values().asScala

  def ownMessages = messages.filter(_.username == name)

  var followers = Set[String]()

  override def receive: Receive = {
    case command: UserCommand => command match {
      case post: Post =>
        storage.put((post.timestamp, idxSeq.incrementAndGet), post)
        followers.foreach(user => sender() ! Posts(List(post), forwardToUser = Some(user)))

      case ListMessages(username) =>
        if (username == name)
          sender() ! Posts(ownMessages)

      case Wall(username) =>
        if (username == name) sender() ! Posts(messages, None, fromWall = true)

      case req: Follow =>
        if (req.from != req.to && req.username == name){
          followers += req.from
          sender() ! Posts(ownMessages, forwardToUser = Some(req.from))
        }

    }
  }
}

object User {

  case class Posts(posts: TraversableOnce[Post], forwardToUser: Option[String] = None, fromWall: Boolean = false)

  object TimestampOrdering extends Comparator[(Long, Long)] {
    override def compare(x: (Long, Long), y: (Long, Long)): Int = (x, y) match {
      case ((xTs, xSq), (yTs, ySq)) if xTs == yTs => ySq.compareTo(xSq)
      case ((xTs, xSq), (yTs, ySq)) => yTs.compareTo(xTs)
    }
  }

  def props(name: String): Props = Props(new User(name))
}