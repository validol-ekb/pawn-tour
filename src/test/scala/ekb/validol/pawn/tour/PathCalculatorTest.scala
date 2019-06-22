package com.truecaller.pawn.tour

import com.truecaller.pawn.tour.PathCalculator.Location
import com.truecaller.pawn.tour.model.PieceSettings
import com.truecaller.pawn.tour.output.OutputInterface
import org.scalatest.FreeSpec

class PathCalculatorTest extends FreeSpec {

  private val stepSettings = PieceSettings(3, 3, 3, 3, 2, 2, 2, 2)
  private val boardSize = 10

  "PathCalculatorTest" - {

    "should calculate valid path for all possible start points" in {
      val errorMatrix = (for {
        y <- 0 until 10
        x <- 0 until 10
      } yield {
        val loc = Location(x, y)
        val pathCalculator = new PathCalculator(boardSize, Location(x, y), stepSettings)
        val result = pathCalculator.run()
        loc -> (result._1.count(_._2.nonEmpty) == boardSize * boardSize)
      }).toMap
      printTestResponse(errorMatrix)
    }

  }

  private def printTestResponse(errorMatrix: Map[Location, Boolean]): Unit = {
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
