package com.tudor.csn

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestProbe}
import com.tudor.csn.Commands.Exit
import com.tudor.csn.User.Posts

class SocialNetworkMasterSpec(_system: ActorSystem) extends SpecBundle(_system) {

  def this() = this(ActorSystem("SocialNetworkMasterSpec"))

  val probe = TestProbe()
  val masterRef: TestActorRef[SocialNetworkMaster] = TestActorRef(SocialNetworkMaster.props(probe.ref))
  val master = masterRef.underlyingActor


  "Master" must {
    "display a welcome message" in {
      probe.expectMsgPF() {
        case s => true
      }
    }

    "create users it does not have" in {
      val username = "Alice"
      master.users.get(username) should be(None)
      masterRef ! username
      awaitCond(master.users.get(username).isDefined)
      probe.expectMsgPF() {
        case s: Posts => true
      }
    }

    "handle user posting" in {
      masterRef ! "Alice -> I love the weather today"
      masterRef ! "Bob -> Damn! We lost!"
      masterRef ! "Bob -> Good game though."
    }

    "handle reading" in {
      masterRef ! "Alice"
      probe.expectMsgPF() {
        case Posts(p, None, false) =>
          p.size should be(1)
          p.foreach(_.username should be("Alice"))
          true
      }

      masterRef ! "Bob"
      probe.expectMsgPF() {
        case Posts(p, None, false) =>
          p.size should be(2)
          p.toList.head.message should be("Good game though.")
          p.foreach(_.username should be("Bob"))
          true
      }

    }

    "say goodbye before exiting" in {
      masterRef ! Exit
      probe.expectMsgPF() {
        case s: String => true
      }
    }
  }

}
