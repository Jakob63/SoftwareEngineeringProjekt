package wizard.model.player

import wizard.model.cards.{Card, Color, Hand}

class BuildAI extends PlayerBuilder {

    private var unfinished: Option[AI] = None
    
    override def setName(name: String): PlayerBuilder = {
      unfinished = Some(AI(name, new Hand(List[Card]()), 0, 0, 0, 0, 0))
        this
    }

    override def reset(): PlayerBuilder = {
        unfinished = None
        this
    }

    override def build(): Player = {
        if (unfinished.isDefined) {
            var player = unfinished.get
            reset()
            return player
        }
        throw new Exception("AI not built yet")
    }
}
