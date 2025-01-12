package wizard.controller.controller_TUI

import wizard.aView.aview_TUI.TextUI
import wizard.actionmanagement.{Observable, Observer}
import wizard.model.player.Player
import wizard.model.rounds.{Game, Round}
import wizard.controller.{RoundState, NormalCardState}

object GameLogic extends Observable {
    add(TextUI)
    def validGame(number: Int): Boolean = {
        number >= 3 && number <= 6
    }

    def playGame(game: Game, players: List[Player]): Unit = {
        for (i <- 1 to game.rounds) { // i = 1, 2, 3, ..., rounds
            val updatedGame = game.copy(currentround = i)
            val round = new Round(players, None, None, new NormalCardState(), 0)
            RoundLogic.playRound(updatedGame.currentround, players)
        }
    }

    // game is over if all rounds are played
    def isOver(game: Game): Boolean = {
        game.rounds == 0
    }
    def enterPlayers(players : List[Player], playerIndex: Int, game: Game): Unit = {
        if (playerIndex < players.length) {
            val player = players(playerIndex)
            notifyObservers("enter player1", players, playerIndex, game)
        }
    }
}
