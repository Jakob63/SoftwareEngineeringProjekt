package wizard.model.player

import wizard.model.cards.{Card, Hand}

import scala.compiletime.uninitialized

class BuildHuman extends PlayerBuilder {

    private var unfinished: Option[Human] = None

    override def setName(name: String): PlayerBuilder = {
      unfinished = Some(Human(name, new Hand(List[Card]()), 0, 0, 0, 0, 0))
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
        throw new Exception("Player not built yet")
    }
}