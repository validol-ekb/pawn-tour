package ekb.validol.pawn.tour

import ekb.validol.pawn.tour.calculator.WarnsdorffsCalculator
import ekb.validol.pawn.tour.config.Config
import ekb.validol.pawn.tour.model.{Chessboard, Tile}
import org.scalatest.FreeSpec

import scala.util.{Failure, Success}

class WarnsdorffsCalculatorTest extends FreeSpec {


  "WarnsdorffsCalculator" - {

    "should calculate valid path for all possible start points" in {
      val errorMatrix = (for {
        y <- 0 until Config.boardSize
        x <- 0 until Config.boardSize
      } yield {
        val tile = Tile(x, y)
        val board = Chessboard(Config.boardSize, Config.pieceSettings)
        WarnsdorffsCalculator.run(tile, board) match {
          case Success(result) =>
            tile -> (result.chessboard.result.count(_._2.nonEmpty) == board.square)
          case Failure(_) =>
            tile -> false
        }
      }).toMap
      printTestResponse(errorMatrix)
    }

  }

  private def printTestResponse(errorMatrix: Map[Tile, Boolean]): Unit = {
    val (str, _) = errorMatrix.toSeq.sortBy(_._1.position).foldLeft((new StringBuilder, 0)) { (acc, item) =>
      val str = if (item._1.y != acc._2) {
        if (item._2) "\n. " else "\nX "
      } else {
        if (item._2) ". " else "X "
      }
      (acc._1.append(str), item._1.y)
    }
    println(str.toString())
    println(s"Failures cnt: ${errorMatrix.count(!_._2)}")
  }

}
