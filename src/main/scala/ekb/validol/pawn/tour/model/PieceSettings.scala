package com.truecaller.pawn.tour.model

import com.truecaller.pawn.tour.model.PieceSettings.Turn

class PieceSettings(n: Int, s: Int, w: Int, e: Int, nw: Int, ne: Int, sw: Int, se: Int) {

  lazy val steps = Seq(
    Turn(n, Direction.N),
    Turn(s, Direction.S),
    Turn(w, Direction.W),
    Turn(e, Direction.E),
    Turn(nw, Direction.NW),
    Turn(ne, Direction.NE),
    Turn(sw, Direction.SW),
    Turn(se, Direction.SE))

}

object PieceSettings {
  case class Turn(size: Int, direction: Direction.Value)

  def apply(n: Int, s: Int, w: Int, e: Int, nw: Int, ne: Int, sw: Int, se: Int): PieceSettings = new PieceSettings(n, s, w, e, nw, ne, sw, se)
}
