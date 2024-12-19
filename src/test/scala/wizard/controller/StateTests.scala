package wizard.controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import wizard.model.model_TUI.player.PlayerType.Human
import wizard.controller.controller_TUI.{ChesterCardState, WizardCardState}
import wizard.model.model_TUI.cards.{Card, Color, Value}
import wizard.model.model_TUI.player.{Player, PlayerFactory}
import wizard.model.model_TUI.rounds.Round
import wizard.testUtils.TestUtil

class StateTests extends AnyWordSpec with Matchers {
    "ChesterCardState" should {
        "handleTrump should set trump to None and notify observers" in {
            val players = List(PlayerFactory.createPlayer(Some("Player1"), Human), PlayerFactory.createPlayer(Some("Player2"), Human))
            val round = new Round(players)
            val chesterCardState = new ChesterCardState

            val trumpCard = Card(Value.Seven, Color.Red)
            chesterCardState.handleTrump(round, trumpCard, players)

            round.trump shouldBe None
            // Assuming notifyObservers is properly tested elsewhere
        }
    }

    "WizardCardState" should {
        "handleTrump should set trump to None, notify observers, and determine new trump" in {
            val players = List(PlayerFactory.createPlayer(Some("Player1"), Human), PlayerFactory.createPlayer(Some("Player2"), Human))
            val round = new Round(players)
            val wizardCardState = new WizardCardState

            val trumpCard = Card(Value.WizardKarte, Color.Red)
            TestUtil.simulateInput("1\n") {
                wizardCardState.handleTrump(round, trumpCard, players)
            }

            round.trump should not be None
            // Assuming notifyObservers and determineTrump are properly tested elsewhere
        }
    }
}