package com.tudor.csn

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}

class ViewerSpec(_system: ActorSystem) extends SpecBundle(_system) {
  def this() = this(ActorSystem("ViewerSpec"))

  val viewerRef = TestActorRef[Viewer]
  val viewer = viewerRef.underlyingActor

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  "SocialNetworkMaster" must {
    "create users it does not have" in {
      viewer.durationToText(1) should be("1 ms")
      viewer.durationToText(1000) should be("1 s")
      viewer.durationToText(1000*60) should be("1 min")
      viewer.durationToText(1000*60*60) should be("1 h")
      viewer.durationToText(1000*60*60*24) should be("1 day(s)")
    }
  }
}
