import java.io.IOException

import domain.{Dialog, Line}
import scalaz.zio.{App, IO}
import scalaz.zio.console._

object ZDrama extends App {

  def run(args: List[String]): IO[Nothing, ExitStatus] =
    parallelDialogs.attempt.map(_.fold(_ => 1, _ => 0)).map(ExitStatus.ExitNow(_))

  def parallelDialogs: IO[Exception, List[Dialog]] =
    for {
      d <- createDialog(100, 1, 1).par(createDialog(100, 2, 1))
      (t1, t2) = d
    } yield List(t1, t2)

  def createDialog(numberOfLines: Int, dialogId: Long, sceneId: Long): IO[Nothing, Dialog] =
    IO.point(Dialog(
      (for {
        i <- 1 to numberOfLines
      } yield Line(
        s"Line with id $i, dialog id $dialogId and scene id: $sceneId written!",
        i,
        dialogId,
        sceneId)).toList,
      dialogId,
      sceneId))
}
