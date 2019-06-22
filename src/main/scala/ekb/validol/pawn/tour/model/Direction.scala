package ekb.validol.pawn.tour.model

object Direction extends Enumeration {
  type Direction = Value
  val N: Value  = Value("n")
  val S: Value  = Value("s")
  val W: Value  = Value("w")
  val E: Value  = Value("e")
  val NW: Value = Value("nw")
  val NE: Value = Value("ne")
  val SW: Value = Value("sw")
  val SE: Value = Value("se")
}
