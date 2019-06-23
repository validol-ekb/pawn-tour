package ekb.validol.pawn.tour

import ekb.validol.pawn.tour.calculator.Calculator
import ekb.validol.pawn.tour.calculator.Calculator.CalculationResult
import ekb.validol.pawn.tour.input.InputInterface
import ekb.validol.pawn.tour.input.InputInterface.InputParameters
import ekb.validol.pawn.tour.model.{Chessboard, Tile}
import ekb.validol.pawn.tour.output.OutputInterface
import org.mockito.{Matchers, Mockito}
import org.mockito.Mockito.{mock, times, verify, when}
import org.scalatest.FreeSpec
import org.scalatest.concurrent._

import scala.concurrent.Future
import scala.util.{Failure, Success}

class PathfinderTest extends FreeSpec with ScalaFutures {

  "Pathfinder" - {

    "should start calculations on success input return" in {
      val workTile = Tile(0, 0)
      val inpMock = mock(classOf[InputInterface])
      val outMock = mock(classOf[OutputInterface[CalculationResult]])
      val calcMock = mock(classOf[Calculator])
      val chessboardMock = mock(classOf[Chessboard])
      val calcResult = CalculationResult(chessboardMock, 0, 0L)
      Mockito.doNothing().when(outMock).onStart()
      Mockito.doNothing().when(outMock).onNext(calcResult)
      when(inpMock.start()).thenReturn(Future.successful(InputParameters(workTile)))
      when(inpMock.continue()).thenReturn(Future.successful(false))
      when(calcMock.run(Matchers.any(classOf[Tile]), Matchers.any(classOf[Chessboard]))).thenReturn(Success(calcResult))

      val pf = Pathfinder(calcMock, inpMock, outMock)
      whenReady(pf.start().future) { _ =>
        verify(outMock, times(1)).onStart()
        verify(outMock, times(1)).onNext(calcResult)
      }
    }

    "should return error on invalid input return" in {
      val inpMock = mock(classOf[InputInterface])
      val outMock = mock(classOf[OutputInterface[CalculationResult]])
      val calcMock = mock(classOf[Calculator])
      val error = new Exception("Error")

      Mockito.doNothing().when(outMock).onStart()
      Mockito.doNothing().when(outMock).onError(error)
      Mockito.doNothing().when(outMock).shutdown()
      when(inpMock.start()).thenReturn(Future.failed(error))

      val pf = Pathfinder(calcMock, inpMock, outMock)

      whenReady(pf.start().future.failed) { err =>
        verify(outMock, times(1)).onStart()
        verify(outMock, times(1)).onError(error)
        assert(err == error)
      }
    }

    "should call onError on exception during calculation" in {
      val workTile = Tile(0, 0)
      val inpMock = mock(classOf[InputInterface])
      val outMock = mock(classOf[OutputInterface[CalculationResult]])
      val calcMock = mock(classOf[Calculator])
      val error = new Exception("Error")
      Mockito.doNothing().when(outMock).onStart()
      Mockito.doNothing().when(outMock).onError(error)
      when(inpMock.start()).thenReturn(Future.successful(InputParameters(workTile)))
      when(inpMock.continue()).thenReturn(Future.successful(false))
      when(calcMock.run(Matchers.any(classOf[Tile]), Matchers.any(classOf[Chessboard]))).thenReturn(Failure(error))

      val pf = Pathfinder(calcMock, inpMock, outMock)
      whenReady(pf.start().future) { _ =>
        verify(outMock, times(1)).onStart()
        verify(outMock, times(1)).onError(error)
      }
    }

    "should call onError if tile is out of range" in {
      val workTile = Tile(100, 100)
      val inpMock = mock(classOf[InputInterface])
      val outMock = mock(classOf[OutputInterface[CalculationResult]])
      val calcMock = mock(classOf[Calculator])

      Mockito.doNothing().when(outMock).onStart()
      Mockito.doNothing().when(outMock).onError(Matchers.any(classOf[String]))
      when(inpMock.start()).thenReturn(Future.successful(InputParameters(workTile)))
      when(inpMock.continue()).thenReturn(Future.successful(false))

      val pf = Pathfinder(calcMock, inpMock, outMock)
      whenReady(pf.start().future) { _ =>
        verify(outMock, times(1)).onStart()
        verify(outMock, times(1)).onError(Matchers.any(classOf[String]))
      }
    }
  }

}
