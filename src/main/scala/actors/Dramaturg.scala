package actors

import actors.Dramaturg._
import actors.Writer._
import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props, SupervisorStrategy}
import domain.{Play, Scene}

import scala.concurrent.duration.Duration

/**
  * Supervises that the writers do a good job. An expert on its field.
  */
class Dramaturg(sceneNumber: Int) extends Actor with ActorLogging {

  var scenes: List[Scene] = List()

  override def receive: Receive = {
    case CreatePlay =>
      log.debug("I shall create the play!")
      // The first step is to create some writers and put them to work!
      for (i <- 1 to sceneNumber) {
        val writer  = context.actorOf(Writer.props(100, 100))
        writer ! WriteScene(i)
      }

    case SceneWritten(scene) =>
      // We got a scene!
      scenes = scenes :+ scene
      // When al scenes are finished send the masterpiece to the director.
      if (scenes.length == sceneNumber) {
        log.debug("Got all scenes, returning a Play!")
        context.parent ! PlayFinished(Play(scenes.sortBy(s => s.sceneId)))
      }
  }

  override val supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = -1, withinTimeRange = Duration.Inf) {
      case _: DialogCorruptedException => Restart
      case _: IndexOutOfBoundsException => Restart
      case _: Exception => Escalate
    }
}

object Dramaturg {
  // Factory method
  def props(sceneNumber: Int): Props = Props(new Dramaturg(sceneNumber))

  // Protocol
  case class PlayFinished(play: Play)
  case object CreatePlay

  // Errors
  case class PlayCorruptedException(msg: String) extends Exception
}
