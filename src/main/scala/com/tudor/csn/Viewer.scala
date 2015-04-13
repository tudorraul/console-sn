package com.tudor.csn

import akka.actor.{Actor, ActorLogging}
import com.tudor.csn.User.Posts

class Viewer extends Actor with ActorLogging {
  override def receive: Receive = {
    case m: Posts =>
      val now = System.currentTimeMillis
      m.posts.foreach(p =>
        display((if (m.fromWall) p.username + " - " else "") + p.message + s" (${durationToText(now - p.timestamp)} ago)"))

    case msg => display(msg.toString)
  }

  def display(msg: String) = println(s"> $msg")

  val durationToText: Long => String = {
    val msInSec = 1000
    val secInMin = 60 * msInSec
    val minInHour = 60 * secInMin
    val hInDay = 24 * minInHour

    {
      case d if d / hInDay > 0 => s"${d / hInDay} day(s)"
      case d if d / minInHour > 0 => s"${d / minInHour} h"
      case d if d / secInMin > 0 => s"${d / secInMin} min"
      case d if d / msInSec > 0 => s"${d / msInSec} s"
      case d => s"$d ms"
    }
  }
}
