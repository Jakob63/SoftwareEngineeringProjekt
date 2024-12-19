package wizard.model.model_TUI.rounds

import wizard.model.model_TUI.player.Player

object Game {
    private var currentGame: Option[Game] = None

    def getCurrentGame: Option[Game] = currentGame

    def setCurrentGame(game: Game): Unit = {
        currentGame = Some(game)
    }
}

case class Game(players: List[Player]) {
    @volatile private var rounds = 60 / players.length
    @volatile private var currentround = 0

    def getRounds: Int = synchronized {
        rounds
    }

    def getCurrentRound: Int = synchronized {
        currentround
    }

    def setCurrentRound(value: Int): Unit = synchronized {
        currentround = value
    }

    players.foreach(player => player.points = 0)
    players.foreach(player => player.tricks = 0)
    players.foreach(player => player.bids = 0)
    players.foreach(player => player.roundPoints = 0)
    players.foreach(player => player.roundBids = 0)
    players.foreach(player => player.roundTricks = 0)
}