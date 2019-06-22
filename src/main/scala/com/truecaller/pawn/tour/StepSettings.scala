package com.truecaller.pawn.tour

import com.truecaller.pawn.tour.StepSettings.Step

class StepSettings(n: Int, s: Int, w: Int, e: Int, nw: Int, ne: Int, sw: Int, se: Int) {

  lazy val steps = Seq(
    Step(n, Direction.N),
    Step(s, Direction.S),
    Step(w, Direction.W),
    Step(e, Direction.E),
    Step(nw, Direction.NW),
    Step(ne, Direction.NE),
    Step(sw, Direction.SW),
    Step(se, Direction.SE))

}

object StepSettings {
  case class Step(size: Int, direction: Direction.Value)

  def apply(n: Int, s: Int, w: Int, e: Int, nw: Int, ne: Int, sw: Int, se: Int): StepSettings = new StepSettings(n, s, w, e, nw, ne, sw, se)
}
