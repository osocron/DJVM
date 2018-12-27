import actors.Director.StartProduction
import actors.Director
import akka.actor.ActorSystem

object Drama extends App {

  val system = ActorSystem.create("actor-system")
  val director = system.actorOf(Director.props())

  director ! StartProduction

}
