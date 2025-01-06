package wizard.controller.controller_TUI.mock

import wizard.controller.controller_TUI.IRoundLogic
import wizard.model.model_TUI.cards.{Card, Color, Value}
import wizard.model.model_TUI.player.Player
import wizard.model.model_TUI.rounds.Round

class Mock_RoundLogic extends IRoundLogic {
  
  override def playRound(round: Int, players: List[Player]): Unit = {
    println(s"Mock RoundLogic: playRound $round")
  }

  override def trickwinner(trick: List[(Player, Card)], round: Round): Player = {
    println("Mock RoundLogic: trickwinner")
    trick.head._1 // Return the first player as the mock winner
  }
}