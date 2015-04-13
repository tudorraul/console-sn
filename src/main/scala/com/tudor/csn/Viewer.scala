package com.tudor.csn

import akka.actor.{Actor, ActorLogging}

class Viewer extends Actor with ActorLogging {
  override def receive: Receive = {
    case msg => display(msg.toString)
  }

  def display(msg: String) = println(s"> $msg")
}
