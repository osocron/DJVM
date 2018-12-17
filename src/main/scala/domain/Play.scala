package domain

import akka.actor.ActorRef

import scala.collection.immutable.ListMap

case class Play(content: List[Scene])
case class Scene(content: List[Dialog])
case class Dialog(content: ListMap[ActorRef, Line])
case class Line(content: String)
