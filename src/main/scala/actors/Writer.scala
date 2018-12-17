package actors

import akka.actor.{Actor, Props}
import domain.{Play, Scene}

class Writer extends Actor {
  override def receive: Receive = ???
}

object Writer {
  // Creates instances of this actor
  def props(): Props = Props(new Writer)

  // The protocol that writers understand
  trait WriterProtocol
  case class WritePlay(play: Play) extends WriterProtocol
  case class PlayScene(scene: Scene) extends WriterProtocol
  case class FinishPlay(play: Play) extends WriterProtocol
}
