package com.tudor.csn

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import com.tudor.csn.Commands.{ListMessages, Post}
import com.tudor.csn.User.Posts

class UserSpec(_system: ActorSystem) extends SpecBundle(_system) {
  def this() = this(ActorSystem("UserSpec"))

  val userRef = TestActorRef[User](User.props("user1"))
  val user = userRef.underlyingActor

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  "User" must {
    "store all posts in inverse chronological order" in {
      user.messages.size should be(0)
      val p1: Post = Post("user1", "some message 1", 1)
      val p2: Post = Post("user1", "some message 2", 2)
      val p3: Post = Post("user2", "some message 3", 3)
      userRef ! p1
      userRef ! p2
      userRef ! p3
      awaitCond(user.messages.size == 3)
      user.messages.head should be(p3)
      user.messages.tail.head should be(p2)
      user.messages.tail.tail.head should be(p1)
    }

    "lists all messages posted by self" in {
      userRef ! ListMessages("user1")
      expectMsgPF() { case p: Posts => p.posts.size == 2}
    }
  }

}
