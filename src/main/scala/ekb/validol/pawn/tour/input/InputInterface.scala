package com.truecaller.pawn.tour.input

import com.truecaller.pawn.tour.input.InputInterface.InputParameters

import scala.concurrent.Future

trait InputInterface {

  def start(): Future[InputParameters]

}

object InputInterface {

  case class InputParameters(positionX: Int, positionY: Int)

}
