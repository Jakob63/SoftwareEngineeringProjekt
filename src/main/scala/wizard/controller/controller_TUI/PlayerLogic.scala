package wizard.controller.controller_TUI

import wizard.aView.aview_TUI.TextUI
import wizard.actionmanagement.{Observable, Observer}
import wizard.model.cards.{Card, Color, Value}
import wizard.model.player.Player

object PlayerLogic extends Observable {
    add(TextUI)

    def playCard(leadColor: Option[Color], trump: Option[Color], currentPlayerIndex: Int, player: Player): Card = {
        notifyObservers("which card", player)
        val cardToPlay = player.playCard(leadColor.orNull, trump.get, currentPlayerIndex)
        if (leadColor.isDefined && cardToPlay.color != leadColor.get && player.hand.hasColor(leadColor.get) && cardToPlay.value != Value.WizardKarte && cardToPlay.value != Value.Chester) {
            notifyObservers("follow lead", leadColor.get)
            playCard(leadColor, trump, currentPlayerIndex, player)
        } else {
            val updatedPlayer = player.withUpdatedHand(player.hand.removeCard(cardToPlay))
            // player.hand = player.hand.removeCard(cardToPlay)
            notifyObservers("player updated", updatedPlayer)
            cardToPlay
        }
    }

    def bid(player: Player): Int = {
        notifyObservers("which bid", player)
        val playersbid = player.bid()
        val updatedPlayer = player.copy(roundbids = playersbid) //TODO: janis welp
        notifyObservers("player updated", updatedPlayer)
        // player.roundBids = playersbid
        playersbid
    }
    //hier war addpoints
}