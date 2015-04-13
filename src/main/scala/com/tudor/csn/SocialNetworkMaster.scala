package com.tudor.csn

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.tudor.csn.Commands._

class SocialNetworkMaster(viewer: ActorRef) extends Actor with ActorLogging {
  val system = context.system

  val commandParser = system.actorOf(Props[CommandParser], "commandParser")

  viewer ! "Welcome to Social Network Demo, type in your commands!\n(type `exit` to exit the demo)"

  override def receive: Receive = {
    case line: String => commandParser ! line

    case Exit =>
      viewer ! "Bye! [Press ENTER]"
      system.shutdown()

    case NotFound => viewer ! "I don't understand"
  }
}

object SocialNetworkMaster {
  def props(viewer: ActorRef) = Props(new SocialNetworkMaster(viewer))
}
