package wizard.controller

import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.{should, shouldBe}
import org.scalatest.wordspec.AnyWordSpec
import wizard.model.model_TUI.cards.{Card, Color, Hand, Value}
import wizard.model.model_TUI.player.{Player, PlayerFactory}
import wizard.model.model_TUI.player.PlayerType.{AI, Human}

class PlayerLogicTest extends AnyWordSpec with Matchers {
    "PlayerLogic" should {

//        "should have a hand" in {
//            val player = PlayerFactory.createPlayer(Some("Test"), Human)
//            player.hand should not be empty
//        }

        "should have 0 points" in {
            val player = PlayerFactory.createPlayer(Some("Test"), Human)
            player.points shouldBe 0
        }

        "should have 0 tricks" in {
            val player = PlayerFactory.createPlayer(Some("Test"), Human)
            player.tricks shouldBe 0
        }

        "should have 0 bids" in {
            val player = PlayerFactory.createPlayer(Some("Test"), Human)
            player.bids shouldBe 0
        }

        "should have 0 roundBids" in {
            val player = PlayerFactory.createPlayer(Some("Test"), Human)
            player.roundBids shouldBe 0
        }

        "should have 0 roundTricks" in {
            val player = PlayerFactory.createPlayer(Some("Test"), Human)
            player.roundTricks shouldBe 0
        }

        "should have 0 roundPoints" in {
            val player = PlayerFactory.createPlayer(Some("Test"), Human)
            player.roundPoints shouldBe 0
        }

        "should add a hand" in {
            val player = PlayerFactory.createPlayer(Some("Test"), Human)
            val hand = Hand(List[Card]())
            player.addHand(hand)
            player.hand shouldBe hand
        }

        "should add tricks" in {
            val player = PlayerFactory.createPlayer(Some("Test"), Human)
            player.addTricks(5)
            player.tricks shouldBe 5
        }
        "should have a random name (AI)" in {
            val player = PlayerFactory.createPlayer(None, AI)
            player.name should (be("Jakob") or be("Elena") or be("Janis") or be("Leon"))
        }
    }
}