package ekb.validol.pawn.tour.output

trait OutputInterface[T] {

  def onStart(): Unit

  def onNext(msg: T): Unit

  def onError(ex: Throwable): Unit

  def shutdown(): Unit

}
