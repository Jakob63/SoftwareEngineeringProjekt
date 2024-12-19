package wizard.controller.controller_TUI

import wizard.aView.aView_TUI.TextUI
import wizard.actionmanagement.{Observable, Observer}
import wizard.controller.controller_TUI.RoundLogic
import wizard.gui.GUI
import wizard.model.model_TUI.player.Player
import wizard.model.model_TUI.rounds.{Game, Round}

object GameLogic extends Observable {

    add(TextUI)
    val gui = new GUI()
    add(gui)
    
    def validGame(number: Int): Boolean = {
        number >= 3 && number <= 6
    }

    def playGame(game: Game, players: List[Player]): Unit = {
        for (i <- 1 to game.getRounds) { // i = 1, 2, 3, ..., rounds
            game.setCurrentRound(i)
            val round = new Round(players)
            RoundLogic.playRound(game.getCurrentRound, players)
        }
    }

    def isOver(game: Game): Boolean = {
        game.getRounds == 0
    }
}