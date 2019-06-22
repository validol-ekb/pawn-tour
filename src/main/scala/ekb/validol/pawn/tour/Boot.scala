package ekb.validol.pawn.tour

import ekb.validol.pawn.tour.input.ConsoleInput
import ekb.validol.pawn.tour.output.ConsoleOutput

object Boot extends App {

  val input = ConsoleInput()
  val output = ConsoleOutput()

  val p = Pathfinder(input, output)
  val closePromise = p.start()

  while (!closePromise.isCompleted) {}

}
