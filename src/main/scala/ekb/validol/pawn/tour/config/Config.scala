package ekb.validol.pawn.tour.config

import ekb.validol.pawn.tour.model.PieceSettings

object Config {

  final val boardSize = 10
  final val pieceSettings = PieceSettings(3, 3, 3, 3, 2, 2, 2, 2)

  final object input {
    val promptMessage = "Specify pawn start coordinates separated by a comma (e.g. 6,4) please "
    val continueMessage = "Would you like to continue calculations? "
    val invalidInputMessage = "Invalid input parameters, please try again"
    val positiveAnswers = Set("yes", "y", "yeah", "yep")
    val negativeAnswers = Set("no", "n", "nah")
  }

  final object output {
    val startMessage = "Pawn pathfinder application started"
    val byeMessage = "Bye bye!"
  }


}
