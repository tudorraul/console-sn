package com.tudor.csn

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import com.tudor.csn.Commands._

class CommandParserSpec(_system: ActorSystem) extends SpecBundle(_system) {

  def this() = this(ActorSystem("CommandParserSpec"))

  val commandParser = system.actorOf(Props[CommandParser])

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  "CommandParser" must {
    "parse user selection" in {
      commandParser ! "username"
      expectMsg(ListMessages("username"))
    }
  }

  it must {
    "parse send message" in {
      commandParser ! "username -> new message here"
      expectMsgPF() { case Post("username", "new message here", _) => true }
    }
  }

  it must {
    "parse follows" in {
      commandParser ! "username follows friend"
      expectMsg(Follow("username", "friend"))
    }
  }

  it must {
    "parse wall" in {
      commandParser ! "username wall"
      expectMsg(Wall("username"))
    }
  }

  it must {
    "parse exit" in {
      commandParser ! "exit"
      expectMsg(Exit)
    }
  }

}
