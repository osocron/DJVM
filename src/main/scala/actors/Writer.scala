package actors

import actors.Dramaturg.CreatePlay
import akka.actor.{Actor, ActorLogging, Props}
import domain._

class Writer extends Actor {
  override def receive: Receive = ???
}

/**
  * A Writer helps create a Play under the direction of
  * the Dramaturg.
  */
object Writer {
  // Creates instances of this actor
  def props(): Props = Props(new Writer)

  // The events produced by the Writers
  trait WriterEvent
  case class LineWritten(dialog: Dialog) extends WriterEvent
  case class DialogWritten(scene: Scene) extends WriterEvent
  case class SceneWritten(play: Play) extends WriterEvent

  // The commands that can be sent to the Writers
  trait WriterCommand
  case class WriteLine(dialog: Dialog) extends WriterCommand
  case class WriteDialog(scene: Scene) extends WriterCommand
  case class WriteScene(play: Play) extends WriterCommand
}

class Dramaturg extends Actor with ActorLogging {
  override def receive: Receive = {
    case CreatePlay =>
      log.debug("I shall create the play!")
  }
}

object Dramaturg {

  // Factory method
  def props(): Props = Props(new Dramaturg)

  // The events produced by the dramaturg
  trait DramaturgEvent
  case class PlayFinished(play: Play) extends DramaturgEvent

  // The commands that can be sent to a Dramaturg
  trait DramaturgCommand
  case object CreatePlay extends DramaturgCommand

}
