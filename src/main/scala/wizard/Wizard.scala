package wizard


import wizard.aView.aview_TUI.TextUI
import wizard.controller.controller_TUI.GameLogic
import wizard.model.cards.Dealer
import wizard.model.rounds.Game

object Wizard {

    class Wizard {

    }

    def main(args: Array[String]): Unit = {
        println("Welcome to Wizard!")
        val players = TextUI.inputPlayers()
        val game = new Game(players)
        println("Game officially started.")
        GameLogic.playGame(game, players)
    }

}