package wizard.controller.controller_TUI.mock

import wizard.controller.controller_TUI.IPlayerLogic
import wizard.model.model_TUI.cards.{Card, Color, Value}
import wizard.model.model_TUI.player.Player

class Mock_PlayerLogic extends IPlayerLogic {
  override def playCard(trumpColor: Option[Color], leadColor: Option[Color], playerIndex: Int, player: Player): Card = {
    println("Mock PlayerLogic: playCard")
    Card(Value.One, Color.Red) // Return a mock card
  }

  override def bid(player: Player): Int = {
    println("Mock PlayerLogic: bid")
    3 // Return a mock bid
  }

  override def addPoints(player: Player): Unit = {
    println("Mock PlayerLogic: addPoints")
    player.points = 40 // Set mock points
  }

  override def calculatePoints(player: Player): Int = {
    println("Mock PlayerLogic: calculatePoints")
    40 // Return mock points
  }
}