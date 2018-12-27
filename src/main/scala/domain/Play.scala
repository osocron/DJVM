package domain

import scala.collection.immutable.ListMap

case class Play(content: List[Scene])
case class Scene(content: List[Dialog])
case class Dialog(content: ListMap[Character, Line])
case class Line(content: String)

case class Character(name: String)
