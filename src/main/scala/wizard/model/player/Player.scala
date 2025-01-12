package wizard.model.player

import wizard.aView.aview_TUI.TextUI
import wizard.model.cards.{Card, Color, Hand}
import wizard.actionmanagement.{Observable, Observer}

abstract case class Player(name: String, hand: Hand, points: Int, tricks: Int, bids: Int, roundBids: Int, roundTricks: Int) extends Observable {
    
    add(TextUI)

    def withUpdatedHand(newHand: Hand) : Player
    def changeName(newName: String) : Player
    def undoPlayCard(card: Card) : Player
    def addPoints(newPoints: Int): Player
    def addHand(newHand: Hand): Player
    def addTricks(newTrick: Int): Player
    def playCard(leadColor: Color, trump: Color, currentPlayerIndex: Int): Card
    def bid(): Int
}
