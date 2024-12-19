package wizard.model.model_TUI.player

import wizard.model.model_TUI.cards.{Card, Color}

class AI private[player](name: String) extends Player(name) {
    
    override def bid(): Int = ???
    override def playCard(leadColor: Color, trump: Color, currentPlayerIndex: Int): Card = ???
}
