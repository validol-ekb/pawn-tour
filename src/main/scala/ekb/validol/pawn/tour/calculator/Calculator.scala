package ekb.validol.pawn.tour.calculator

import ekb.validol.pawn.tour.calculator.Calculator.{CalculationResult, TemporaryResult}
import ekb.validol.pawn.tour.model.{Chessboard, Tile}

import scala.util.Try

trait Calculator {

  final def run(tile: Tile, chessboard: Chessboard): Try[CalculationResult] = {
    val start = System.currentTimeMillis()
    Try(calculate(tile, chessboard)).map { tmp =>
      CalculationResult(tmp.chessboard, tmp.iterations, System.currentTimeMillis() - start)
    }
  }

  protected def calculate(tile: Tile, chessboard: Chessboard): TemporaryResult

}

object Calculator {

  case class CalculationResult(chessboard: Chessboard, iterations: Int, time: Long)
  private[calculator] case class TemporaryResult(chessboard: Chessboard, iterations: Int)

}
