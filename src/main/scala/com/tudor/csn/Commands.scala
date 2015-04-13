package com.tudor.csn

sealed trait Command

sealed trait UserCommand extends Command {
  val username: String
}

object Commands {

  case class ListMessages(username: String) extends UserCommand

  case class Wall(username: String) extends UserCommand

  case class Post(username: String, message: String, timestamp: Long = System.currentTimeMillis) extends UserCommand

  case class Follow(from: String, to: String) extends UserCommand {
    val username = to
  }

  case object Exit extends Command

  case object NotFound extends Command

}

