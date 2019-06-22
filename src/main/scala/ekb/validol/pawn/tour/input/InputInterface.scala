package ekb.validol.pawn.tour.input

import ekb.validol.pawn.tour.input.InputInterface.InputParameters
import ekb.validol.pawn.tour.model.Tile

import scala.concurrent.Future

trait InputInterface {

  def start(): Future[InputParameters]

}

object InputInterface {

  case class InputParameters(tile: Tile)

}
