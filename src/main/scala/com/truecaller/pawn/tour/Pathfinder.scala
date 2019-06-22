package com.truecaller.pawn.tour

import com.truecaller.pawn.tour.PathCalculator.Location
import com.truecaller.pawn.tour.input.InputInterface
import com.truecaller.pawn.tour.output.OutputInterface

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise
import scala.util.{Failure, Success}

class Pathfinder(input: InputInterface, output: OutputInterface[Map[Location, Option[Int]]]) {

  private val closePromise = Promise[Unit]
  private val stepSettings = StepSettings(3, 3, 3, 3, 2, 2, 2, 2)

  private def startCalculations(parameters: InputInterface.InputParameters): Unit = {
    val calculator = new PathCalculator(10, Location(parameters.positionX, parameters.positionY), stepSettings, output)
    val chessboard = calculator.start()
    println(chessboard._1.size.toString)
    println(chessboard._1.count(_._2.nonEmpty))
    println(s"Iterations cnt: ${chessboard._2}")
    //output.onNext(chessboard.size.toString)
    //output.onNext(chessboard.count(_._2.nonEmpty).toString)
    output.onNext(chessboard._1)
    closePromise.trySuccess()
  }

  def start(): Promise[Unit] = {
    input.start().onComplete {
      case Success(parameters) =>
        startCalculations(parameters)
      case Failure(exception) =>
        output.onError(exception)
        output.shutdown()
    }
    closePromise
  }

}

object Pathfinder {

  def apply(input: InputInterface, output: OutputInterface[Map[Location, Option[Int]]]): Pathfinder = new Pathfinder(input, output)

}
