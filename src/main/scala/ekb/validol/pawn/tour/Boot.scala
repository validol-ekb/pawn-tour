package ekb.validol.pawn.tour

import ekb.validol.pawn.tour.calculator.WarnsdorffsCalculator
import ekb.validol.pawn.tour.input.ConsoleInput
import ekb.validol.pawn.tour.output.ConsoleOutput

object Boot extends App {

  val input = ConsoleInput()
  val output = ConsoleOutput()

  val p = Pathfinder(WarnsdorffsCalculator, input, output)
  val closePromise = p.start()

  while (!closePromise.isCompleted) {}

}
