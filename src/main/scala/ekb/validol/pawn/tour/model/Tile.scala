package ekb.validol.pawn.tour.model

case class Tile(x: Int, y: Int) extends Ordered[Tile] {

  private[model] lazy val position: Int = s"$y$x".toInt

  override def compare(that: Tile): Int = this.position compare that.position
}
