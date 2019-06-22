package ekb.validol.pawn.tour.model

import ekb.validol.pawn.tour.model.PieceSettings.Turn

class Chessboard private (val boardSize: Int, pieceSettings: PieceSettings, private val fields: Map[Tile, Option[Int]]) {

  lazy val square: Int = boardSize * boardSize

  def findTiles(tile: Tile): Seq[Tile] = {
    pieceSettings.turns.foldLeft(Seq.empty[Tile]) { (acc, turn) =>
      turn match {
        case Turn(v, Direction.N) => acc ++ checkTile(tile.copy(y = tile.y - v))
        case Turn(v, Direction.S) => acc ++ checkTile(tile.copy(y = tile.y + v))
        case Turn(v, Direction.W) => acc ++ checkTile(tile.copy(x = tile.x - v))
        case Turn(v, Direction.E) => acc ++ checkTile(tile.copy(x = tile.x + v))
        case Turn(v, Direction.NW) => acc ++ checkTile(tile.copy(x = tile.x - v, y = tile.y - v))
        case Turn(v, Direction.NE) => acc ++ checkTile(tile.copy(x = tile.x + v, y = tile.y - v))
        case Turn(v, Direction.SW) => acc ++ checkTile(tile.copy(x = tile.x - v, y = tile.y + v))
        case Turn(v, Direction.SE) => acc ++ checkTile(tile.copy(x = tile.x + v, y = tile.y + v))
        case _ => acc
      }
    }
  }

  def checkTile(tile: Tile): Option[Tile] = {
    fields.get(tile) match {
      case Some(None) => Some(tile)
      case _ => None
    }
  }

  def update(tile: Tile, rank: Int): Chessboard = {
    copy(fields.updated(tile, Some(rank)))
  }

  def remove(tile: Tile): Chessboard = {
    copy(fields.updated(tile, None))
  }

  def result: Map[Tile, Option[Int]] = fields

  private def copy(fields: Map[Tile, Option[Int]]): Chessboard = {
    new Chessboard(boardSize, pieceSettings, fields)
  }
}

object Chessboard {

  private def generateBoard(boardSize: Int): Map[Tile, Option[Int]] = {
    (for {
      y <- 0 until boardSize
      x <- 0 until boardSize
    } yield {
      Tile(x, y) -> None
    }).toMap
  }

  def apply(boardSize: Int, pieceSettings: PieceSettings): Chessboard = {
    val board = generateBoard(boardSize)
    new Chessboard(boardSize, pieceSettings, board)
  }

}
