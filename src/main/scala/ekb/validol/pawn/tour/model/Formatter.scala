package ekb.validol.pawn.tour.model

import ekb.validol.pawn.tour.calculator.Calculator.CalculationResult

sealed trait Formatter[T] {

  def print(msg: T): String

}

object Formatter {

  implicit object CalculationResultFormatter extends Formatter[CalculationResult] {

    override def print(msg: CalculationResult): String = {
      val builder = new StringBuilder
      builder.append("\n")

      val (str, _) = msg.chessboard.result.toSeq.sortBy(_._1.position).foldLeft((builder, 0)){ case (acc, item) =>
        val str = if (item._1.y != acc._2) {
          item._2 match {
            case Some(v) if v < 10 => s"\n0$v "
            case Some(v) => s"\n$v "
            case None => "\n.. "
          }
        } else {
          item._2 match {
            case Some(v) if v < 10 => s"0$v "
            case Some(v) => s"$v "
            case None => ".. "
          }
        }
        (acc._1.append(str), item._1.y)
      }
      str.append("\n\n")
      str.append(s"Iterations count: ${msg.iterations}")
      str.append("\n")
      str.append(s"Calculation time: ${msg.time} ms")
      str.append("\n")

      str.toString()
    }

  }

}
