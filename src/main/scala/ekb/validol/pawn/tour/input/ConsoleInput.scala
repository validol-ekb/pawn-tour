package ekb.validol.pawn.tour.input

import ekb.validol.pawn.tour.model.Tile

import scala.concurrent.{Future, Promise}
import scala.io.StdIn

class ConsoleInput extends InputInterface {

  override def start(): Future[InputInterface.InputParameters] = {
    val p = Promise[InputInterface.InputParameters]
    Console.println("Pawn tour application started")

    val position = StdIn.readLine("Specify pawn position please ")

    try {
      val pos = position.split(",").map(_.trim.toInt).take(2)
      p.trySuccess(InputInterface.InputParameters(Tile(pos.head, pos(1))))
    } catch {
      case e: Exception =>
        p.tryFailure(e)
    }

    p.future
  }
}

object ConsoleInput {

  def apply(): ConsoleInput = new ConsoleInput()

}
