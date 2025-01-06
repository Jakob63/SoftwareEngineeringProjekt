package wizard.controller

import wizard.model.cards.{Card, Color, Value}
import wizard.model.player.Player
import wizard.gui.WizardApp
import wizard.actionmanagement.{Observable, Observer}

object GUIPlayerLogic extends Observable {
    add(WizardApp)

    def playCard(leadColor: Option[Color], trump: Option[Color], currentPlayerIndex: Int, player: Player): Card = {
        notifyObservers("which card", player)
        val cardToPlay = player.playCard(leadColor.orNull, trump.get, currentPlayerIndex)
        if (leadColor.isDefined && cardToPlay.color != leadColor.get && player.hand.hasColor(leadColor.get) && cardToPlay.value != Value.WizardKarte && cardToPlay.value != Value.Chester) {
            notifyObservers("follow lead", leadColor.get)
            return playCard(leadColor, trump, currentPlayerIndex, player)
        } else {
            player.hand = player.hand.removeCard(cardToPlay)
            cardToPlay
        }
    }

    def bid(player: Player): Int = {
        notifyObservers("which bid", player)
        player.roundBids
    }

    def addPoints(player: Player): Unit = {
        if (player.roundBids == player.roundTricks) {
            player.points += 20 + 10 * player.roundBids
        } else {
            player.points -= 10 * Math.abs(player.roundBids - player.roundTricks)
        }
    }

    def calculatePoints(player: Player): Int = {
        val points = player.roundPoints
        val bids = player.roundBids
        val tricks = player.roundTricks
        if (bids == tricks) {
            points + 20 + tricks * 10
        } else {
            points - Math.abs(bids - tricks) * 10
        }
    }
}