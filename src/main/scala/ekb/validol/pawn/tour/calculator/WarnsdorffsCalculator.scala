package ekb.validol.pawn.tour.calculator

import ekb.validol.pawn.tour.calculator.Calculator.TemporaryResult
import ekb.validol.pawn.tour.model.{Chessboard, Tile}

import scala.annotation.tailrec

object WarnsdorffsCalculator extends Calculator {

  override protected def calculate(tile: Tile, chessboard: Chessboard): TemporaryResult = {
    val cb = chessboard.update(tile, 0)
    calculate(tile, cb.findTiles(tile), 1, Map(0 -> Seq(tile)), cb, 0)
  }

  @tailrec
  private def calculate(tile: Tile, nextTiles: Seq[Tile], step: Int, path: Map[Int, Seq[Tile]],
                        chessboard: Chessboard, counter: Int): TemporaryResult = {

    def goBack(t: Tile, b: Chessboard, s: Int, p: Map[Int, Seq[Tile]]) = {
      (path(Math.max(s - 2, 0)).head, b.remove(t), s - 1)
    }

    if (nextTiles.nonEmpty) {
      val possibleSteps = nextTiles.foldLeft(Map.empty[Tile, Seq[Tile]]) { (acc, t) =>
        acc + (t -> chessboard.findTiles(t))
      }

      val optimalSteps = possibleSteps
        .map {
          case (l, paths) => l -> paths.size
        }.groupBy(_._2).toSeq.sortBy(_._1)
         .headOption.map(_._2.keys.toSeq)
         .getOrElse(Seq.empty).sortBy(evaluateStep(chessboard.boardSize))

      val (nextStep, updatedPath) = path.get(step) match {
        case Some(previousLocations) =>
          (optimalSteps diff previousLocations).headOption match {
            case Some(v) => (Some(v), v +: previousLocations)
            case None => (None, previousLocations)
          }
        case None => optimalSteps.headOption match {
          case Some(v) => (Some(v), Seq(v))
          case None => (None, Seq.empty)
        }
      }

      nextStep match {
        case Some(s) =>
          calculate(s, possibleSteps(s), step + 1, path.updated(step, updatedPath), chessboard.update(s, step), counter + 1)
        case None =>
          val (prevTile, board, prevStep) = goBack(tile, chessboard, step, path)
          if (prevStep < 0) {
            throw new RuntimeException(s"Can't solve task for $tile")
          } else {
            calculate(prevTile, board.findTiles(prevTile), prevStep, path.updated(step, updatedPath), board, counter + 1)
          }
      }

    } else {
      if (path.size + 1 <= chessboard.square) {
        val (prevTile, board, prevStep) = goBack(tile, chessboard, step, path)
        if (prevStep < 0) {
          throw new RuntimeException(s"Can't solve task for $tile")
        } else {
          calculate(prevTile, board.findTiles(prevTile), prevStep, path, board, counter + 1)
        }
      } else {
        TemporaryResult(chessboard, counter + 1)
      }
    }
  }

  private def evaluateStep(boardSize: Int)(t: Tile): Int = {
    val xMin = Math.min(boardSize - t.x - 1, t.x)
    val yMin = Math.min(boardSize - t.y - 1, t.y)
    Math.min(xMin, yMin)
  }

}
