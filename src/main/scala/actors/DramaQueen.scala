package actors

import akka.actor.{Actor, Props}

class DramaQueen extends Actor {
  override def receive: Receive = ???
}

object DramaQueen {
  def props(): Props = Props(new DramaQueen())
}
