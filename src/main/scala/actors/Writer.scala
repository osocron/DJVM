package actors

import actors.Dramaturg.{CreatePlay, PlayFinished}
import actors.Writer._
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import domain._

import scala.collection.immutable.ListMap

class Writer extends Actor {

  var lines: List[Line] = List()
  var dialogs: List[Dialog] = List()

  override def receive: Receive = {

    // Reacting to commands
    case WriteLine(lineId, dialogId, sceneId) =>
      sender() ! LineWritten(Line(s"Line with id $lineId and $dialogId written!", lineId, dialogId, sceneId))

    case WriteDialog(dialogId, sceneId) =>
      // Help is needed! Let's spawn 10 more writers!
      for (i <- 1 to 10) {
        val colleague = context.actorOf(Writer.props())
        colleague ! WriteLine(i, dialogId, sceneId)
      }

    case WriteScene(sceneId) =>
      // Help is needed! Let's spawn 10 more writers!
      for (i <- 1 to 10) {
        val colleague = context.actorOf(Writer.props())
        colleague ! WriteDialog(i, sceneId)
      }

    // One of our writers finished a line
    case LineWritten(line) =>
      lines = lines :+ line
      // When all lines are collected, send the result to the parent
      if (lines.length == 10) {
        val dialog = lines.sortBy(l => l.lineId).foldLeft(ListMap.empty[Character, Line])((acc, next) => acc + (Character("Someone") -> next) )
        context.parent ! DialogWritten(Dialog(dialog, line.dialogId, line.sceneId))
      }

    // One of the writers finished a dialog
    case DialogWritten(dialog) =>
      dialogs = dialogs :+ dialog
      // When all dialogs are collected, send the result to the parent
      if (dialogs.length == 10) {
        context.parent ! SceneWritten(Scene(dialogs, dialog.sceneId))
      }

  }
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
  case class LineWritten(line: Line) extends WriterEvent
  case class DialogWritten(dialog: Dialog) extends WriterEvent
  case class SceneWritten(scene: Scene) extends WriterEvent

  // The commands that can be sent to the Writers
  trait WriterCommand
  case class WriteLine(lineId: Long, dialogId: Long, sceneId: Long) extends WriterCommand
  case class WriteDialog(dialogId: Long, sceneId: Long) extends WriterCommand
  case class WriteScene(sceneId: Long) extends WriterCommand
}

class Dramaturg extends Actor with ActorLogging {

  var scenes: List[Scene] = List()

  override def receive: Receive = {
    case CreatePlay =>
      log.debug("I shall create the play!")
      // The first step is to create some writers and put them to work!
      for (i <- 1 to 10) {
        val writer  = context.actorOf(Writer.props())
        writer ! WriteScene(i)
      }
    case SceneWritten(scene) =>
      // We got a scene
      scenes = scenes :+ scene
      if (scenes.length == 10) {
        context.parent ! PlayFinished(Play(scenes))
      }
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
