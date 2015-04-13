package com.tudor.csn

import akka.actor.{Actor, ActorLogging, Props}

class User(name: String) extends Actor with ActorLogging {

  override def receive: Receive = {
    case command: UserCommand =>
  }
}

object User {
  def props(name: String): Props = Props(new User(name))
}