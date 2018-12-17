package protocol

import domain.Line

/**
  * This is the protocol that Actors know how to to talk
  */
trait ActorProtocol
case class InterpretDialog(line: Line) extends ActorProtocol
case object ShowEmotionalResponse extends ActorProtocol
