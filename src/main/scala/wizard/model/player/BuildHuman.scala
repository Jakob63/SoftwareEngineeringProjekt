package wizard.model.player

import scala.compiletime.uninitialized

class BuildHuman extends PlayerBuilder {

    private var unfinished: Option[Human] = None

    override def setName(name: String): PlayerBuilder = {
        if (unfinished.isEmpty) {
            unfinished = Some(Human(name))
        } else {
            unfinished.get.name = name
        }
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