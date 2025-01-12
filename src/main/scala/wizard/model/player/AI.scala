package wizard.model.player

import wizard.model.player.Player
import wizard.model.cards.{Card, Color, Hand}

class AI private[player](name: String, hand: Hand, points: Int, tricks: Int, bids: Int, roundBids: Int, roundTricks: Int) extends Player(name, hand, points, tricks, bids, roundBids, roundTricks) {
    
    override def changeName(newName: String) : Player = {
        new AI(newName, hand, points, tricks, bids, roundBids, roundTricks)
    }
    
    override def addPoints(newPoints: Int): Player = {
        if (roundBids == roundTricks) {
            new AI( name, hand, points + 20 + 10 * roundBids, tricks, bids, roundBids, roundTricks)
        } else {
            new AI( name, hand, points - 10 * Math.abs(roundBids - roundTricks), tricks, bids, roundBids, roundTricks) 
        }
    }
    
    override def addHand(newHand: Hand): Player = {
        new AI(name, newHand, points, tricks, bids, roundBids, roundTricks)
    }
    
    override def addTricks(newTrick: Int) : Player = {
        new AI(name, hand, points, tricks + newTrick, bids, roundBids, roundTricks)
    }
    override def bid(): Player = {
        new AI(name, hand, points, tricks, (Math.random() * 4).toInt , roundBids, roundTricks)
    }
    
    override def playCard(leadColor: Color, trump: Color, currentPlayerIndex: Int): Unit = { // TODO: Unit ok? oder Card?
        val cardIndex = (Math.random() * hand.cards.length).toInt
        hand.cards(cardIndex)
    }
    
    override def undoPlayCard(card: Card) : Player = {
        new AI(name, hand.addCards(List(card)), points, tricks, bids, roundBids, roundTricks)
    }
}
