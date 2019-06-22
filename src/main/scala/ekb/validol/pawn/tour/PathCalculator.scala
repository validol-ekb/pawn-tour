package ekb.validol.pawn.tour

import ekb.validol.pawn.tour.PathCalculator.Location
import ekb.validol.pawn.tour.model.PieceSettings.Turn
import ekb.validol.pawn.tour.model.{Direction, PieceSettings}

import scala.annotation.tailrec

class PathCalculator(boardSize: Int, location: Location, stepSettings: PieceSettings) {

  def run(): (Map[Location, Option[Int]], Int) = {

    val board = generateBoard()
    calculateV2(location, findLocations(location, board), 1, Map(0 -> Seq(location)), board, 0)
//    val result = calculate(location, findLocations(location, board), 1, board, false)(locationMinEvaluator)
//    if (result.exists(_._2.isEmpty)) {
//      val board2 = generateBoard()
//      calculate(location, findLocations(location, board2), 1, board2, true)(locationMinEvaluator)
//    } else result
  }

  private def generateBoard(): Map[Location, Option[Int]] = {
    (for {
      y <- 0 until boardSize
      x <- 0 until boardSize
    } yield {
      Location(x, y) -> (if (x == location.x && y == location.y) Some(0) else None)
    }).toMap
  }

  @tailrec
  private def calculate(loc: Location, nextLocations: Set[Location], step: Int, board: Map[Location, Option[Int]], withEvaluator: Boolean)(locationEvaluator: Seq[Location] => Location): Map[Location, Option[Int]] = {
    if (nextLocations.nonEmpty) {
      val possibleSteps = nextLocations.foldLeft(Map.empty[Location, Set[Location]]) { (acc, l) =>
        acc + (l -> findLocations(l, board))
      }

      val nextStep = if (withEvaluator) {
        val nextSteps = possibleSteps.map { case (l, paths) =>
          l -> paths.size
        }.toSeq.groupBy(_._2).toSeq.sortBy(_._1).headOption.map(_._2.map(_._1)).getOrElse(Seq.empty)
        locationEvaluator(nextSteps)
      } else {
        possibleSteps.map { case (l, paths) =>
          l -> paths.size
        }.toSeq.minBy(_._2)._1
      }

      val upd = board.updated(nextStep, Some(step))
      calculate(nextStep, possibleSteps(nextStep), step + 1, upd, withEvaluator)(locationEvaluator)
      //calculate(nextStep, possibleSteps(nextStep), step + 1, upd)
    } else board
  }

  @tailrec
  private def calculateV2(loc: Location, nextLocations: Set[Location], step: Int, path: Map[Int, Seq[Location]],
                          board: Map[Location, Option[Int]], iterations: Int): (Map[Location, Option[Int]], Int) = {
    if (nextLocations.nonEmpty) {
      val possibleSteps = nextLocations.foldLeft(Map.empty[Location, Set[Location]]) { (acc, l) =>
        acc + (l -> findLocations(l, board))
      }

      val optimalSteps: Seq[Location] = possibleSteps.map { case (l, paths) =>
        l -> paths.size
      }.groupBy(_._2).toSeq.sortBy(_._1).headOption.map(_._2.keys.toSeq)
      .getOrElse(Seq.empty).sortBy{ l =>
        val xMin = Math.min(boardSize - l.x - 1, l.x)
        val yMin = Math.min(boardSize - l.y - 1, l.y)
        Math.min(xMin, yMin)
      }

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
          calculateV2(s, possibleSteps(s), step + 1, path.updated(step, updatedPath), board.updated(s, Some(step)), iterations + 1)
        case None =>
          val s = Math.max(step - 2, 0)
          val stepBack = path(s).head
          val b = board.updated(loc, None)
          val prevStep = step - 1
          if (prevStep < 0) {
            throw new RuntimeException(s"Can't solve task for $location")
          } else {
            calculateV2(stepBack, findLocations(stepBack, b), prevStep, path.updated(step, updatedPath), b, iterations + 1)
          }
      }
    } else {
      if (path.size + 1 <= boardSize * boardSize) {
        //Oops huston we have a problem
        val b = board.updated(loc, None)
        val s = Math.max(step - 2, 0)
        val stepBack = path(s).head
        val prevStep = step - 1
        if (prevStep < 0) {
          throw new RuntimeException(s"Can't solve task for $location")
        } else {
          calculateV2(stepBack, findLocations(stepBack, b), prevStep, path, b, iterations + 1)
        }

      } else {
        (board, iterations + 1)
      }
    }
  }

  private def locationMinEvaluator(locations: Seq[Location]): Location = {
    locations.minBy { l =>
      val xMin = Math.min(boardSize - l.x - 1, l.x)
      val yMin = Math.min(boardSize - l.y - 1, l.y)
      Math.min(xMin, yMin)
    }
  }

  private def findLocations(l: Location, board: Map[Location, Option[Int]]): Set[Location] = {
    stepSettings.turns.foldLeft(Set.empty[Location]) { (acc, s) =>
      s match {
        case Turn(v, Direction.N) => acc ++ checkLocation(l.copy(y = l.y - v), board)
        case Turn(v, Direction.S) => acc ++ checkLocation(l.copy(y = l.y + v), board)
        case Turn(v, Direction.W) => acc ++ checkLocation(l.copy(x = l.x - v), board)
        case Turn(v, Direction.E) => acc ++ checkLocation(l.copy(x = l.x + v), board)
        case Turn(v, Direction.NW) => acc ++ checkLocation(l.copy(x = l.x - v, y = l.y - v), board)
        case Turn(v, Direction.NE) => acc ++ checkLocation(l.copy(x = l.x + v, y = l.y - v), board)
        case Turn(v, Direction.SW) => acc ++ checkLocation(l.copy(x = l.x - v, y = l.y + v), board)
        case Turn(v, Direction.SE) => acc ++ checkLocation(l.copy(x = l.x + v, y = l.y + v), board)
        case _ => acc
      }
    }
  }

  private def checkLocation(l: Location, board: Map[Location, Option[Int]]): Option[Location] = {
    board.get(l) match {
      case Some(None) => Some(l)
      case _ => None
    }
  }

}

object PathCalculator {

  case class Location(x: Int, y: Int) {
    def position: Int = s"$y$x".toInt
  }

}
