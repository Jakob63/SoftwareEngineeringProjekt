package wizard.model.rounds

import wizard.aView.aview_TUI.TextUI
import wizard.model.cards.{Card, Color, Dealer, Value}
import wizard.model.player.Player

import scala.compiletime.uninitialized
import wizard.controller.RoundState
import wizard.actionmanagement.Observable
// TODO: case class gemacht
case class Round(players: List[Player], trump: Option[Color], leadColor: Option[Color], state: RoundState, currentPlayerIndex: Int) extends Observable {

    add(TextUI) // Added den Observer

    def withLeadColor(newLeadColor: Option[Color]): Round = {
        copy(leadColor = newLeadColor)
    }

    def withTrump(newTrump: Option[Color]): Round = {
        copy(trump = newTrump)
    }
    
    def getCurrentPlayerIndex: Int = currentPlayerIndex

    def setTrump(newTrump: Option[Color]): Round = {
        new Round(players, newTrump, leadColor, state, currentPlayerIndex)
    }

    def setState(newState: RoundState): Round = {
        new Round(players, trump, leadColor, newState, currentPlayerIndex)
    }

    def handleTrump(newTrumpCard: Card, players: List[Player]): Round = {
        new Round(players, Some(newTrumpCard.color), leadColor, state, currentPlayerIndex)
    }

    def nextPlayer(): Round = {
        new Round(players, trump, leadColor, state, (currentPlayerIndex + 1) % players.length)
    }

    override def toString: String = {
        s"Trump: $trump, LeadColor: $leadColor, CurrentPlayerIndex: $currentPlayerIndex, Players: $players"
    }

}