package domain

import scala.collection.immutable.ListMap

case class Play(content: List[Scene])
case class Scene(content: List[Dialog], sceneId: Long)
case class Dialog(content: ListMap[Character, Line], dialogId: Long, sceneId: Long)
case class Line(content: String, lineId: Long, dialogId: Long, sceneId: Long)

case class Character(name: String)
