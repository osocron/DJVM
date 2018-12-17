package actors

import akka.actor.{Actor, ActorLogging, Props}

class Spectator extends Actor with ActorLogging {

  override def preStart(): Unit = super.preStart()

  override def postStop(): Unit = super.postStop()

  override def receive: Receive = ???
}

object Spectator {
  def props(): Props = Props(new Spectator())
}
