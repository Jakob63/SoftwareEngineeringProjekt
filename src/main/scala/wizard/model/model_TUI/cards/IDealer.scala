package wizard.model.model_TUI.cards

trait IDealer {

  /**
   * Shuffles the cards in the deck.
   * @return true if the cards were shuffled successfully, false otherwise
   */
  def shuffleCards(): Boolean
  
  /**
   * Deals a certain amount of cards to a player.
   * @param cards_amount the amount of cards to deal
   * @param excludeCard the card to exclude from the dealing
   * @return the hand of the player
   */
  def dealCards(cards_amount: Int, excludeCard: Option[Card] = None): Hand
  
}
