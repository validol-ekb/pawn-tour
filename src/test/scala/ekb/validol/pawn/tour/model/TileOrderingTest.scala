package ekb.validol.pawn.tour.model

import org.scalatest.{FreeSpec, Matchers}

class TileOrderingTest extends FreeSpec with Matchers {

  "Tiles" - {

    "should be sorted by their coordinates" in {
      val seq = Seq(Tile(0, 2), Tile(1, 0), Tile(0, 0), Tile(5, 1), Tile(4, 2))

      seq.sorted should contain theSameElementsInOrderAs Seq(Tile(0, 0), Tile(1, 0), Tile(5, 1), Tile(0, 2), Tile(4, 2))
    }

  }

}
