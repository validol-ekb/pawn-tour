package ekb.validol.pawn.tour

import ekb.validol.pawn.tour.calculator.Calculator.CalculationResult
import ekb.validol.pawn.tour.calculator.Calculator
import ekb.validol.pawn.tour.config.Config
import ekb.validol.pawn.tour.input.InputInterface
import ekb.validol.pawn.tour.model.Chessboard
import ekb.validol.pawn.tour.output.OutputInterface

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise
import scala.util.{Failure, Success}
import ekb.validol.pawn.tour.model.Formatter._

class Pathfinder(calculator: Calculator, input: InputInterface, output: OutputInterface[CalculationResult]) {

  private val closePromise = Promise[Unit]

  def start(): Promise[Unit] = {
    output.onStart()

    input.start().onComplete {
      case Success(parameters) =>
        startCalculations(parameters)
      case Failure(exception) =>
        output.onError(exception)
        output.shutdown()
        closePromise.tryFailure(exception)
    }
    closePromise
  }

  private def startCalculations(parameters: InputInterface.InputParameters): Unit = {
    val chessboard = Chessboard(Config.boardSize, Config.pieceSettings)

    chessboard.checkTile(parameters.tile) match {
      case Some(t) =>
        calculator.run(t, chessboard) match {
          case Success(result) =>
            output.onNext(result)
          case Failure(exception) =>
            output.onError(exception)
        }
      case _ =>
        output.onError(s"Tile ${parameters.tile.x}, ${parameters.tile.y} is out of range")
    }

    input.continue().onComplete {
      case Success(true) => input.start().map(startCalculations)
      case _ =>
        output.shutdown()
        closePromise.trySuccess(())
    }
  }

}

object Pathfinder {

  def apply(calculator: Calculator, input: InputInterface, output: OutputInterface[CalculationResult]): Pathfinder = {
    new Pathfinder(calculator, input, output)
  }

}
