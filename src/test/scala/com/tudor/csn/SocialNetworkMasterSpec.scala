package com.tudor.csn

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestProbe}

class SocialNetworkMasterSpec(_system: ActorSystem) extends SpecBundle(_system) {

  def this() = this(ActorSystem("SocialNetworkMasterSpec"))

  val probe = TestProbe()
  val masterRef: TestActorRef[SocialNetworkMaster] = TestActorRef(SocialNetworkMaster.props(probe.ref))
  val master = masterRef.underlyingActor


  it must {
    "create users it does not have" in {
      val username = "Alice"
      master.users.get(username) should be(None)
      masterRef ! username
      awaitCond(master.users.get(username).isDefined)
    }
  }

}
