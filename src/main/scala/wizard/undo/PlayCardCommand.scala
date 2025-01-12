package wizard.undo

import wizard.model.player.Player
import wizard.model.cards.{Card,Color}
// TODO: Janis?
class PlayCardCommand (player: Player, leadColor: Color, trump: Color, currentPlayerIndex: Int) extends Command {
  private var playedCard: Option[Card] = None
  
  override def doStep(): Unit = {
        playedCard = Some(player.playCard(leadColor, trump, currentPlayerIndex))
    }

    override def undoStep(): Unit = {
      playedCard.foreach(card => player.undoPlayCard(card))
    }
}
