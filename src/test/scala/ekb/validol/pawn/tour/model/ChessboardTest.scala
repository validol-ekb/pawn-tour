package ekb.validol.pawn.tour.model

import ekb.validol.pawn.tour.config.Config
import org.scalatest.{FreeSpec, Matchers}

class ChessboardTest extends FreeSpec with Matchers {

  "Chessboard" - {

    "should initialize empty Map on start" in {
      val cb = Chessboard(Config.boardSize, Config.pieceSettings)
      assert(cb.square == Config.boardSize * Config.boardSize)
      assert(cb.result.size == Config.boardSize * Config.boardSize)
      assert(cb.result.count(_._2.nonEmpty) == 0)
    }

    "should evaluate tiles by range and accessibility" in {
      val workTile = Tile(6,6)
      val cb1 = Chessboard(Config.boardSize, Config.pieceSettings)
      assert(cb1.checkTile(Tile(100, 100)).isEmpty)
      assert(cb1.checkTile(workTile).nonEmpty)
      val cb2 = cb1.update(workTile, 1)
      assert(cb2.checkTile(workTile).isEmpty)
      val cb3 = cb2.remove(workTile)
      assert(cb3.checkTile(workTile).nonEmpty)
    }

    "should find all possible paths for specified tile" in {
      val workTile = Tile(2, 3)
      val cb1 = Chessboard(Config.boardSize, Config.pieceSettings)
      val possibleTurns1 = cb1.findTiles(workTile)

      possibleTurns1 should contain theSameElementsAs Seq(
        Tile(2,0), Tile(0,1), Tile(4,1), Tile(5,3), Tile(4, 5), Tile(2, 6), Tile(0, 5)
      )

      val cb2 = cb1.update(Tile(4, 1), 1)
      val possibleTurns2 = cb2.findTiles(workTile)

      possibleTurns2 should contain theSameElementsAs Seq(
        Tile(2,0), Tile(0,1), Tile(5,3), Tile(4, 5), Tile(2, 6), Tile(0, 5)
      )
    }

  }

}
