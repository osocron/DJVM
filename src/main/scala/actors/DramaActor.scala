package actors

import akka.actor.{Actor, Props}

class DramaActor extends Actor {
  override def receive: Receive = ???
}

object DramaActor {
  def props(): Props = Props(new DramaActor())
}

