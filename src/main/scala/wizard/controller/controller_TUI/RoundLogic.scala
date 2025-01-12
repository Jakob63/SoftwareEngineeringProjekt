package wizard.controller.controller_TUI

import wizard.aView.aview_TUI.TextUI
import wizard.aView.aview_TUI.TextUI.showHand
import wizard.actionmanagement.{Observable, Observer}
import wizard.controller.{ChesterCardState, NormalCardState, WizardCardState}
import wizard.controller.controller_TUI.PlayerLogic
import wizard.model.cards.{Card, Color, Dealer, Value}
import wizard.model.player.Player
import wizard.model.rounds.Round

object RoundLogic extends Observable {
    add(TextUI)

    def playRound(currentround: Int, players: List[Player]): Unit = {
        var round = new Round(players, None, None, new NormalCardState, 0) // davor Round(players) und val TODO: so?
        val trumpCardIndex = currentround * players.length
        val trumpCard = if (trumpCardIndex < Dealer.allCards.length) {
            Dealer.allCards(trumpCardIndex)
        } else {
            throw new IndexOutOfBoundsException("No trump card available.")
        }

        trumpCard.value match {
            case Value.Chester => round.setState(new ChesterCardState)
            case Value.WizardKarte => round.setState(new WizardCardState)
            case _ => round.setState(new NormalCardState)
        }

        Dealer.shuffleCards()
        players.foreach { player =>
            val hand = Dealer.dealCards(currentround, Some(trumpCard))
            player.addHand(hand)
        }
        notifyObservers("cards dealt")
        players.foreach(showHand)

        round = round.handleTrump(trumpCard, players)

        players.foreach(player => player.bid()) // bid(players) davor
        for (i <- 1 to currentround) {
            round = round.withLeadColor(None)
            var trick = List[(Player, Card)]()
            var firstPlayerIndex = 0

            while (round.leadColor.isEmpty && firstPlayerIndex < players.length) {
                val player = players(firstPlayerIndex)
                val card = player.playCard(round.leadColor.getOrElse(round.trump.getOrElse(Color.Red)), round.trump.getOrElse(Color.Red), firstPlayerIndex)
                if (card.value != Value.WizardKarte && card.value != Value.Chester) {
                    round = round.withLeadColor(Some(card.color))
                }
                trick = trick :+ (player, card)
                firstPlayerIndex += 1
            }

            for (j <- firstPlayerIndex until players.length) {
                val player = players(j)
                val card = player.playCard(round.leadColor.getOrElse(Color.Red), round.trump.getOrElse(Color.Red), j)
                trick = trick :+ (player, card)
            }

            trick.foreach { case (player, _) =>
                if (!player.hand.isEmpty) {
                    showHand(player)
                }
            }
            val winner = trickwinner(trick, round)
            notifyObservers("trick winner", winner)
            // winner.roundTricks += 1
            winner.addTricks(1) //TODO: so?
        }
        
        val updatedPlayers = players.map(player=> player.addPoints(player.roundTricks))
        notifyObservers("points after round")
        notifyObservers("print points all players", players)
    }

    def trickwinner(trick: List[(Player, Card)], round: Round): Player = {
        val leadColor = trick.head._2.color
        val trump = round.trump
        val leadColorCards = trick.filter(_._2.color == leadColor)
        val trumpCards = trick.filter(_._2.color == trump)
        val winningCard = if (trumpCards.nonEmpty) {
            trumpCards.maxBy(_._2.value.ordinal)
        } else {
            leadColorCards.maxBy(_._2.value.ordinal)
        }
        winningCard._1
    }
}