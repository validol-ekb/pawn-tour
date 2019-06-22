package com.truecaller.pawn.tour.calculator

import com.truecaller.pawn.tour.PathCalculator.Location
import com.truecaller.pawn.tour.calculator.Calculator.CalculationResult

import scala.util.Try

trait Calculator {

  def run(): Try[CalculationResult]

}

object Calculator {

  case class CalculationResult(chessboard: Map[Location, Option[Int]], iterations: Int, time: Long)

}
