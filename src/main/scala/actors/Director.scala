package actors

import akka.actor.{Actor, Props}

class Director extends Actor {
  override def receive: Receive = ???
}

object Director {
  def props(): Props = Props(new Director)

  trait DirectorProtocol
  case object Action extends DirectorProtocol
  case object Rehearse extends DirectorProtocol
}
