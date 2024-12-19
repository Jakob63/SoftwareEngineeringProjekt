package wizard.controller.controller_TUI

import wizard.aView.aView_TUI.TextUI
import wizard.actionmanagement.{Observable, Observer}
import wizard.controller.controller_TUI.RoundState
import wizard.model.model_TUI.cards.{Card, Color, Value}
import wizard.model.model_TUI.player.Player
import wizard.model.model_TUI.rounds.Round

trait RoundState extends Observable {
    def handleTrump(round: Round, trumpCard: Card, players: List[Player]): Unit
}

class NormalCardState extends RoundState {
    override def handleTrump(round: Round, trumpCard: Card, players: List[Player]): Unit = {
        round.setTrump(Some(trumpCard.color))
        round.notifyObservers("print trump card", trumpCard)
    }
}

class ChesterCardState extends RoundState {
    override def handleTrump(round: Round, trumpCard: Card, players: List[Player]): Unit = {
        round.setTrump(None)
        round.notifyObservers("print trump card", trumpCard)
    }
}

class WizardCardState extends RoundState {
    override def handleTrump(round: Round, trumpCard: Card, players: List[Player]): Unit = {
        round.setTrump(None)
        round.notifyObservers("print trump card", trumpCard)
        determineTrump(round, players)
    }

    private def determineTrump(round: Round, players: List[Player]): Unit = {
        val nextPlayer = players(round.currentPlayerIndex)
        val colorOptions = List(Color.Red, Color.Yellow, Color.Green, Color.Blue)
        val colorCards = colorOptions.map(color => Card(Value.One, color))

        // Print color options
        printColorOptions(colorCards)

        def printColorOptions(cards: List[Card]): Unit = {
            cards.zipWithIndex.foreach { case (card, index) =>
                println(s"${index + 1}. $card")
            }
        }
        
        val input = TextUI.update("which trump", nextPlayer).asInstanceOf[String]
        val chosenColorIndex = input.toInt - 1
        val chosenColor = colorOptions.lift(chosenColorIndex)

        round.setTrump(chosenColor)
        round.notifyObservers("print trump card", Card(Value.One, chosenColor.getOrElse(Color.Red)))
    }
}