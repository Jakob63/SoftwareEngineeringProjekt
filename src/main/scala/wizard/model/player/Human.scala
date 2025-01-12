package wizard.model.player

import wizard.model.cards.{Card, Color, Hand}

import scala.util.{Failure, Success, Try}

class Human private[player](name: String, hand: Hand, points: Int, tricks: Int, bids: Int, roundBids: Int, roundTricks: Int) extends Player(name, hand, points, tricks, bids, roundBids, roundTricks) {

    override def changeName(newName: String) : Player = {
        new Human(newName, hand, points, tricks, bids, roundBids, roundTricks)
    }
    
    override def addPoints(newPoints: Int): Player = {
        if (roundBids == roundTricks) {
            new Human( name, hand, points + 20 + 10 * roundBids, tricks, bids, roundBids, roundTricks)
        } else {
            new Human( name, hand, points - 10 * Math.abs(roundBids - roundTricks), tricks, bids, roundBids, roundTricks) 
        }
    }
    
    override def addHand(newHand: Hand): Player = {
        new Human(name, newHand, points, tricks, bids, roundBids, roundTricks)
    }
    
    override def addTricks(newTrick: Int): Player = {
        new Human(name, hand, points, tricks + newTrick, bids, roundBids, roundTricks)
    }
    
    override def bid(): Int = {
        val input = notifyObservers("bid einlesen").asInstanceOf[String]
        if (input == "" || input.trim.isEmpty || !input.forall(_.isDigit)) {
            notifyObservers("invalid input, bid again")
            return bid()
        }
        input.toInt
    }

    override def playCard(leadColor: Color, trump: Color, currentPlayerIndex: Int): Card = { // TODO: Card oke? oder doch Unit lassen
        val input = notifyObservers("card einlesen").asInstanceOf[String]
        val cardIndex = Try(input.toInt) match {
            case Success(index) => index
            case Failure(_) => -1
        }
        if (cardIndex < 1 || cardIndex > hand.cards.length) {
            notifyObservers("invalid card")
            return playCard(leadColor, trump, currentPlayerIndex)
        }
        hand.cards(cardIndex - 1)
    }
    
    override def undoPlayCard(card: Card): Player = { // TODO: ist hand. addCard oke? (auch der import dazu oke)
        new Human(name, hand.addCards(List(card)), points, tricks, bids, roundBids, roundTricks)
    }

    override def withUpdatedHand(newHand: Hand): Player = {
        new Human(name, newHand, points, tricks, bids, roundBids, roundTricks)
        // oder so:
        // copy(hand = newHand)
    }
}
