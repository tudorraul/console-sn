package com.tudor.csn

import akka.actor.{Props, ActorSystem}

import scala.io.StdIn

object Bootstrap extends App {
  val system = ActorSystem("SocialNetworkDemo")

  val master = system.actorOf(Props[SocialNetworkMaster], "SocialNetworkMaster")

  Iterator.continually(StdIn.readLine()).takeWhile(_.nonEmpty).foreach(master ! _)

  system.awaitTermination()
}
