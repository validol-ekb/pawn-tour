package ekb.validol.pawn.tour.model

case class Tile(x: Int, y: Int) {

  def position: Int = s"$y$x".toInt

}
