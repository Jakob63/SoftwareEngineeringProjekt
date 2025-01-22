package wizard.controller.controller_TUI.base

import wizard.aView.aView_TUI.TextUI
import wizard.actionmanagement.{Observable, Observer}
import wizard.controller.controller_TUI.IGameLogic
import wizard.gui.GUI
import wizard.model.model_TUI.player.Player
import wizard.model.model_TUI.rounds.{Game, Round}

object GameLogic extends Observable with IGameLogic {

    add(TextUI)
    val gui = new GUI()
    add(gui)
    
    override def validGame(number: 3 | 4 | 5 | 6): Boolean = {
        number >= 3 && number <= 6
    }

    override def playGame(game: Game, players: List[Player]): Unit = {
        for (i <- 1 to game.getRounds) { // i = 1, 2, 3, ..., rounds
            game.setCurrentRound(i)
            val round = new Round(players)
            RoundLogic.playRound(game.getCurrentRound, players)
        }
    }

    override def isOver(game: Game): Boolean = {
        game.getRounds == 0
    }
}