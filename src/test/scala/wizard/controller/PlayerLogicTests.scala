package wizard.controller

import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.shouldBe
import org.scalatest.wordspec.AnyWordSpec
import wizard.controller.controller_TUI.IPlayerLogic
import wizard.controller.controller_TUI.base.RoundLogic.playerlogic
import wizard.controller.controller_TUI.base.PlayerLogic
import wizard.model.model_TUI.cards.{Card, Color, Hand, Value}
import wizard.model.model_TUI.player.PlayerFactory
import wizard.testUtils.TestUtil
import wizard.model.model_TUI.player.PlayerType.Human
import wizard.controller.controller_TUI.mock.Mock_PlayerLogic

class PlayerLogicTests extends AnyWordSpec with Matchers {
    private var playerlogic: IPlayerLogic = new Mock_PlayerLogic
    
    "PlayerLogic" should {

        "play a valid card when an invalid card is attempted first" in {
            val player = PlayerFactory.createPlayer(Some("TestPlayer"), Human)
            val hand = Hand(List(Card(Value.One, Color.Red), Card(Value.One, Color.Blue)))
            player.addHand(hand)
            var card: Option[Card] = None
            TestUtil.simulateInput("2\n1\n") {
                card = Some(playerlogic.playCard(Some(Color.Red), Some(Color.Blue), 0, player))
            }
            card shouldBe Some(Card(Value.One, Color.Red))
        }
        "bid correctly" in { // wrong bid and right bid
            val player = PlayerFactory.createPlayer(Some("TestPlayer"), Human)
            val out = new java.io.ByteArrayOutputStream()
            Console.withOut(out) {
                TestUtil.simulateInput("\n3\n") {
                    val bid = playerlogic.bid(player)
                    bid shouldBe 3
                }
            }
        }
        "add points correctly when bids match tricks" in {
            val player = PlayerFactory.createPlayer(Some("TestPlayer"), Human)
            player.roundBids = 2
            player.roundTricks = 2
            playerlogic.addPoints(player)
            player.points shouldBe 40
        }

        "subtract points correctly when bids do not match tricks" in {
            val player = PlayerFactory.createPlayer(Some("TestPlayer"), Human)
            player.roundBids = 2
            player.roundTricks = 1
            playerlogic.addPoints(player)
            player.points shouldBe -10
        }

        "calculate points correctly when bids match tricks" in {
            val player = PlayerFactory.createPlayer(Some("TestPlayer"), Human)
            player.roundBids = 2
            player.roundTricks = 2
            val points = playerlogic.calculatePoints(player)
            points shouldBe 40
        }

        "calculate points correctly when bids do not match tricks" in {
            val player = PlayerFactory.createPlayer(Some("TestPlayer"), Human)
            player.roundBids = 2
            player.roundTricks = 1
            val points = playerlogic.calculatePoints(player)
            points shouldBe -10
        }
    }
}