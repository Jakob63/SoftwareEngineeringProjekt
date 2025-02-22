// Round.scala
package wizard.model.rounds

import wizard.model.cards.{Color, Dealer, Value}
import wizard.model.player.Player
import scala.compiletime.uninitialized

class Round(players: List[Player]) {
    // Aktueller Trumpf
    var trump: Color = uninitialized
    var leadColor: Option[Color] = None
    var currentPlayerIndex = 0
    
    // Methode zum Setzen des Trumpfs
    def setTrump(trump: Color): Unit = {
        this.trump = trump
    }

    def nextPlayer(): Player = {
        val player = players(currentPlayerIndex)
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length
        // next player
        player
    }

    // is game over
    def isOver(): Boolean = {
        players.forall(player => player.hand.isEmpty)
    }

    // finalize round
    def finalizeRound(): Unit = {
        players.foreach(player => player.points += player.roundPoints)
        players.foreach(player => player.tricks += player.roundTricks)
        players.foreach(player => player.bids += player.roundBids)
        players.foreach(player => player.roundPoints = 0)
        players.foreach(player => player.roundTricks = 0)
        players.foreach(player => player.roundBids = 0)
    }

    override def toString: String = {
        s"Trump: $trump, LeadColor: $leadColor, CurrentPlayerIndex: $currentPlayerIndex, Players: $players"
    }
    
}