package wizard.controller.controller_TUI

import wizard.model.model_TUI.cards.{Card, Color}
import wizard.model.model_TUI.player.Player
import wizard.model.model_TUI.rounds.Round

trait IRoundLogic {
  
  def playRound(round: Int, players: List[Player]): Unit
  def trickwinner(trick: List[(Player, Card)], round: Round): Player
  
}
