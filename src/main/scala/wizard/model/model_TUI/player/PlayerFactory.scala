package wizard.model.model_TUI.player

enum PlayerType:
    case Human, AI
end PlayerType

object PlayerFactory {
    def createPlayer(name: Option[String], playerType: PlayerType): Player = {
        val buildType: PlayerBuilder = playerType match {
            case PlayerType.Human => new BuildHuman()
            case PlayerType.AI => new BuildAI()
        }
        name match {
            case Some(playerName) => Director.makeWithName(buildType, playerName)
            case None => Director.makeRandomNames(buildType)
        }
    }
}