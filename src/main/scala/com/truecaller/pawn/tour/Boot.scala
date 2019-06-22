package com.truecaller.pawn.tour

import com.truecaller.pawn.tour.input.ConsoleInput
import com.truecaller.pawn.tour.output.ConsoleOutput

object Boot extends App {

  val input = ConsoleInput()
  val output = ConsoleOutput()

  val p = Pathfinder(input, output)
  val closePromise = p.start()

  while (!closePromise.isCompleted) {}

}
