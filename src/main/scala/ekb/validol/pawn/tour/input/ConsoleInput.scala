package ekb.validol.pawn.tour.input

import ekb.validol.pawn.tour.config.Config
import ekb.validol.pawn.tour.input.ConsoleInput.InvalidInputException
import ekb.validol.pawn.tour.model.Tile

import scala.concurrent.Future
import scala.io.StdIn
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.NoStackTrace

class ConsoleInput extends InputInterface {

  override def start(): Future[InputInterface.InputParameters] = Future {
    val tile = showDialog(Config.input.promptMessage) { msg =>
      msg.split(",").map(_.trim.toInt).take(2) match {
        case Array(x, y) => Tile(x, y)
        case _ => throw InvalidInputException
      }
    }
    InputInterface.InputParameters(tile)
  }

  override def continue(): Future[Boolean] = Future {
    showDialog(Config.input.continueMessage) { msg =>
      msg.toLowerCase() match {
        case m if Config.input.positiveAnswers.contains(m) => true
        case m if Config.input.negativeAnswers.contains(m) => false
        case _ => throw InvalidInputException
      }
    }
  }

  private def showDialog[T](msg: String)(cb: String => T): T = {
    val result = StdIn.readLine(msg)
    try {
      cb(result)
    } catch {
      case _: Throwable =>
        println(Config.input.invalidInputMessage)
        showDialog(msg)(cb)
    }
  }
}

object ConsoleInput {

  object InvalidInputException extends Throwable with NoStackTrace

  def apply(): ConsoleInput = new ConsoleInput()

}
