package com.truecaller.pawn.tour.input
import scala.concurrent.{Future, Promise}
import scala.io.StdIn

class ConsoleInput extends InputInterface {

  override def start(): Future[InputInterface.InputParameters] = {
    val p = Promise[InputInterface.InputParameters]
    Console.println("Pawn tour application started")

    val position = StdIn.readLine("Specify pawn position please ")

    try {
      println(position)
      val pos = position.split(",").map(_.trim.toInt).take(2)
      p.trySuccess(InputInterface.InputParameters(pos.head, pos(1)))
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
