package actors

import actors.Director.StartProduction
import actors.Dramaturg._
import akka.actor.{Actor, ActorLogging, Props}

class Director extends Actor with ActorLogging {

  override def receive: Receive = {
    case StartProduction =>
      log.debug("Starting the magnificent production...")
      val dramaturg = context.actorOf(Dramaturg.props(100))
      log.debug("Telling our dramaturg to start creating a play...")
      dramaturg ! CreatePlay
    case PlayFinished(play) =>
      log.debug(s"I received a complete play!")
  }

}

object Director {
  def props(): Props = Props(new Director)

  // Protocol
  case object ProductionStarted
  case object StartProduction
}
