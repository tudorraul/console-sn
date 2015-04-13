package com.tudor.csn

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestKitBase}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class SpecBundle(_system: ActorSystem) extends TestKit(_system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {
  this: TestKitBase ⇒
}
