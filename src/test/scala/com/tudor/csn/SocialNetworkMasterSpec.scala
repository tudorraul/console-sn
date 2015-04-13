package com.tudor.csn

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestActorRef, TestProbe}
import com.tudor.csn.Commands._
import com.tudor.csn.User.Posts

class SocialNetworkMasterSpec(_system: ActorSystem) extends SpecBundle(_system) {

  def this() = this(ActorSystem("SocialNetworkMasterSpec"))

  val probe = TestProbe()
  val masterRef: TestActorRef[SocialNetworkMaster] = TestActorRef(SocialNetworkMaster.props(probe.ref))
  val master = masterRef.underlyingActor

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

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

    "allow followers to see all messages from people they follow" in {
      masterRef ! "Charlie -> I'm in New York today! Anyone want to have a coffee?"
      masterRef ! "Charlie follows Alice"
      masterRef ! "Charlie follows Bob"
      masterRef ! "Charlie wall"
      probe.expectMsgPF() {
        case Posts(p, None, true) =>
          p.size should be(4)

          val p0: Post = p.toList(0)
          val p1: Post = p.toList(1)
          val p2: Post = p.toList(2)
          val p3: Post = p.toList(3)

          p0.username should be("Charlie")
          p1.username should be("Bob")
          p2.username should be("Bob")
          p3.username should be("Alice")

          p0.timestamp should be >= p1.timestamp
          p1.timestamp should be >= p2.timestamp
          p2.timestamp should be >= p3.timestamp

          true
      }

    }

    "followers continue to see all messages from people they follow" in {
      masterRef ! "Alice -> Sun is up!"
      masterRef ! "Charlie wall"
      probe.expectMsgPF() {
        case Posts(p, None, true) =>
          p.size should be(5)
          p.toList.head.username should be("Alice")
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
