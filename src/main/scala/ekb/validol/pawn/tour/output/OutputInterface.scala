package ekb.validol.pawn.tour.output

import ekb.validol.pawn.tour.model.Formatter

trait OutputInterface[T] {

  def onStart(): Unit

  def onNext(msg: T)(implicit formatter: Formatter[T]): Unit

  def onError(ex: Throwable): Unit

  def onError(msg: String): Unit

  def shutdown(): Unit

}
