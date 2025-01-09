package wizard.model.player

import aWizard.model.cards.{Card, Color}
import wizard.model.player.Player

class AI private[player](name: String) extends Player(name) {
    
    override def bid(): Int = ???
    override def playCard(leadColor: Color, trump: Color, currentPlayerIndex: Int): Card = ???
}
