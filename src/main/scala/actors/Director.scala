package actors

import actors.Director.StartProduction
import actors.Dramaturg.{CreatePlay, PlayFinished}
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

class Director extends Actor with ActorLogging {

  var dramaturg: ActorRef = _

  override def receive: Receive = {
    case StartProduction =>
      log.debug("Starting the magnificent production...")
      dramaturg = context.actorOf(Dramaturg.props())
      log.debug("Telling our dramaturg to start creating a play...")
      dramaturg ! CreatePlay
    case PlayFinished(play) =>
      println(play)
  }

}

object Director {

  def props(): Props = Props(new Director)

  trait DirectorEvents
  case object ProductionStarted extends DirectorEvents

  trait DirectorCommands
  case object StartProduction extends DirectorCommands

}
