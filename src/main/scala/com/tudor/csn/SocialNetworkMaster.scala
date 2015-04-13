package com.tudor.csn

import akka.actor.{Props, Actor, ActorLogging}

class SocialNetworkMaster extends Actor with ActorLogging {
  val system = context.system

  val commandParser = system.actorOf(Props[CommandParser], "commandParser")

  override def receive: Receive = {
    case line: String => commandParser ! line

  }
}
