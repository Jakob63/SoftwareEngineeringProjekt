package wizard.controller.controller_TUI

import wizard.model.model_TUI.cards.{Card, Color}
import wizard.model.model_TUI.player.Player

trait IPlayerLogic {
  
  def playCard(leadColor: Option[Color], trump: Option[Color], currentPlayerIndex: Int, player: Player): Card
  def bid(player: Player): Int
  def addPoints(player: Player): Unit
  def calculatePoints(player: Player): Int
}
