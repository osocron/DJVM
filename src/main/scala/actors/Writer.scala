package actors

import actors.Writer._
import akka.actor.{Actor, ActorLogging, Props}
import domain._

class Writer(numberOfScenes: Int, numberOfLines: Int) extends Actor with ActorLogging {

  var dialogs: List[Dialog] = List()

  var lines: List[Line] = List()

  override def receive: Receive = {

    case WriteScene(sceneId) =>
      // Help is needed! Let's spawn 10 more writers!
      for (i <- 1 to numberOfScenes) {
        val colleague = context.actorOf(Writer.props(100, 100))
        colleague ! WriteDialog(i, sceneId)
      }

    case WriteDialog(dialogId, sceneId) =>
      context.parent ! DialogWritten(
        Dialog(
          (for {
            i <- 1 to numberOfLines
          } yield Line(
            s"Line with id $i, dialog id $dialogId and scene id: $sceneId written!",
            i,
            dialogId,
            sceneId)).toList,
          dialogId,
          sceneId))

    // One of our writers finished a line
    case LineWritten(line) =>
      lines = lines :+ line
      // When all lines are collected, send the result to the parent
      if (lines.size == numberOfLines) {
        context.parent ! DialogWritten(Dialog(lines.sortBy(l => l.lineId), line.dialogId, line.sceneId))
      }

    // One of the writers finished a dialog
    case DialogWritten(dialog) =>
      dialogs = dialogs :+ dialog
      // When all dialogs are collected, send the result to the parent
      if (dialogs.size == numberOfScenes) {
        context.parent ! SceneWritten(Scene(dialogs.sortBy(d => d.dialogId), dialog.sceneId))
      }
  }
}

object Writer {
  def props(numberOfScenes: Int, numberOfLines: Int): Props = Props(new Writer(numberOfScenes, numberOfLines))

  // Protocol
  case class WriteLine(lineId: Long, dialogId: Long, sceneId: Long)
  case class WriteDialog(dialogId: Long, sceneId: Long)
  case class WriteScene(sceneId: Long)
  case class LineWritten(line: Line)
  case class DialogWritten(dialog: Dialog)
  case class SceneWritten(scene: Scene)

  // Errors
  case class DialogCorruptedException(msg: String) extends Exception
}