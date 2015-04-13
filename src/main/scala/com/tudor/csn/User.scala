package com.tudor.csn

import java.util
import java.util.Comparator

import akka.actor.{Actor, ActorLogging, Props}
import com.tudor.csn.Commands.Post

class User(name: String) extends Actor with ActorLogging {

  import collection.JavaConverters._

  var storage = new util.TreeMap[Long, Post](User.TimestampOrdering)

  def messages = storage.values().asScala

  override def receive: Receive = {
    case command: UserCommand => command match {
      case post: Post =>
        storage.put(post.timestamp, post)
    }
  }
}

object User {

  object TimestampOrdering extends Comparator[Long] {
    override def compare(x: Long, y: Long): Int = y.compareTo(x)
  }

  def props(name: String): Props = Props(new User(name))
}