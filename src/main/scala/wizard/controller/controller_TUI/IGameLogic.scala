package wizard.controller.controller_TUI

import wizard.model.model_TUI.player.Player
import wizard.model.model_TUI.rounds.Game

trait IGameLogic {
  
  def validGame(number: 3|4|5|6): Boolean
  def playGame(game: Game, players: List[Player]): Unit
  def isOver(game: Game): Boolean
}
