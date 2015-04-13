package com.tudor.csn

import akka.actor.{Props, ActorSystem}

import scala.io.StdIn

object Bootstrap extends App {
  val system = ActorSystem("SocialNetworkDemo")

  val viewer = system.actorOf(Props[Viewer], "viewer")

  val master = system.actorOf(SocialNetworkMaster.props(viewer), "SocialNetworkMaster")

  Iterator.continually(StdIn.readLine()).takeWhile(_.nonEmpty).foreach(master ! _)

  system.awaitTermination()
}
