package ekb.validol.pawn.tour.output

import ekb.validol.pawn.tour.calculator.Calculator.CalculationResult
import ekb.validol.pawn.tour.config.Config
import ekb.validol.pawn.tour.model.Formatter

class ConsoleOutput extends OutputInterface[CalculationResult] {
  override def onStart(): Unit = {
    println(Config.output.startMessage)
  }

  override def onNext(msg: CalculationResult)(implicit formatter: Formatter[CalculationResult]): Unit = {
    println(formatter.print(msg))
  }

  override def onError(ex: Throwable): Unit = {
    println(String.valueOf(ex))
  }

  override def onError(msg: String): Unit = {
    println(msg)
  }

  override def shutdown(): Unit = {
    println(Config.output.byeMessage)
  }
}

object ConsoleOutput {

  def apply(): ConsoleOutput = new ConsoleOutput()

}
