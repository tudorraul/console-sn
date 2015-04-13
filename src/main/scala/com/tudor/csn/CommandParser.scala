package com.tudor.csn

import akka.actor.{Actor, ActorLogging}
import com.tudor.csn.Commands._

class CommandParser extends Actor with ActorLogging {
  val postParser = "(\\w+) -> (.*)".r
  val followsParser = "(\\w+) follows (\\w+)".r
  val listParser = "(\\w+)".r
  val wallParser = "(\\w+) wall".r
  val exitWord = "exit"

  override def receive: Receive = {
    case line: String =>
      val command = line match {
        case postParser(username, message) => Post(username, message)
        case followsParser(from, to) => Follow(from, to)
        case wallParser(username) => Wall(username)
        case s if s == exitWord => Exit
        case listParser(username) => ListMessages(username)
        case _ => Commands
      }
      sender() ! command
  }
}
