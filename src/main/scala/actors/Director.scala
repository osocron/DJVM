package actors

import actors.Director.StartProduction
import actors.Dramaturg._
import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props, SupervisorStrategy}

import scala.concurrent.duration._

/**
  * A director has under his direction pretty much everybody.
  * His is the most important person on the theater and supervises
  * everybody else.
  */
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

  override val supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = -1, withinTimeRange = Duration.Inf) {
      case _: PlayCorruptedException => Restart
      case _: IndexOutOfBoundsException => Restart
      case _: Exception => Escalate
    }

}

object Director {
  def props(): Props = Props(new Director)

  // Protocol
  case object ProductionStarted
  case object StartProduction
}
