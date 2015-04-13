package com.tudor.csn

import akka.actor.ActorSystem

object Bootstrap extends App {
  val system = ActorSystem("SocialNetworkDemo")

  system.awaitTermination()
}
