package ekb.validol.pawn.tour.output

import ekb.validol.pawn.tour.model.Tile

class ConsoleOutput extends OutputInterface[Map[Tile, Option[Int]]] {
  override def onStart(): Unit = {
    println("Started calculation")
  }

  override def onNext(msg: Map[Tile, Option[Int]]): Unit = {
    println(msg.toSeq.sortBy(_._1.position))
    val (str, _) = msg.toSeq.sortBy(_._1.position).foldLeft((new StringBuilder, 0)){ case (acc, item) =>
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
    println(str.toString())
  }

  override def onError(ex: Throwable): Unit = {
    println(String.valueOf(ex))
  }

  override def shutdown(): Unit = {
    println("Bye bye!")
  }
}

object ConsoleOutput {

  def apply(): ConsoleOutput = new ConsoleOutput()

}
