package ekb.validol.pawn.tour

import ekb.validol.pawn.tour.calculator.WarnsdorffsCalculator
import ekb.validol.pawn.tour.config.Config
import ekb.validol.pawn.tour.input.InputInterface
import ekb.validol.pawn.tour.model.{Chessboard, Tile}
import ekb.validol.pawn.tour.output.OutputInterface

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise
import scala.util.{Failure, Success}

class Pathfinder(input: InputInterface, output: OutputInterface[Map[Tile, Option[Int]]]) {

  private val closePromise = Promise[Unit]

  private def startCalculations(parameters: InputInterface.InputParameters): Unit = {
    val chessboard = Chessboard(Config.boardSize, Config.pieceSettings)
    WarnsdorffsCalculator.run(parameters.tile, chessboard) match {
      case Success(value) =>
        output.onNext(value.chessboard.result)
        println(s"Calculation time: ${value.time} ms")
        println(s"Iterations count: ${value.iterations}")
      case Failure(exception) =>
        output.onError(exception)
    }
    output.shutdown()
    closePromise.trySuccess()
  }

  def start(): Promise[Unit] = {
    input.start().onComplete {
      case Success(parameters) =>
        startCalculations(parameters)
      case Failure(exception) =>
        output.onError(exception)
        output.shutdown()
    }
    closePromise
  }

}

object Pathfinder {

  def apply(input: InputInterface, output: OutputInterface[Map[Tile, Option[Int]]]): Pathfinder = new Pathfinder(input, output)

}
