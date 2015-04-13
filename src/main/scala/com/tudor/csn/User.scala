package com.tudor.csn

import java.util
import java.util.Comparator

import akka.actor.{Actor, ActorLogging, Props}
import com.tudor.csn.Commands.{ListMessages, Post}
import com.tudor.csn.User.Posts

class User(name: String) extends Actor with ActorLogging {

  import collection.JavaConverters._

  var storage = new util.TreeMap[Long, Post](User.TimestampOrdering)

  def messages = storage.values().asScala

  override def receive: Receive = {
    case command: UserCommand => command match {
      case post: Post =>
        storage.put(post.timestamp, post)

      case ListMessages(username) =>
        if (username == name)
          sender() ! Posts(messages)
    }
  }
}

object User {

  case class Posts(posts: TraversableOnce[Post], forwardToUser: Option[String] = None, fromWall: Boolean = false)

  object TimestampOrdering extends Comparator[Long] {
    override def compare(x: Long, y: Long): Int = y.compareTo(x)
  }

  def props(name: String): Props = Props(new User(name))
}