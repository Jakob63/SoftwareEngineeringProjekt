package wizard

import wizard.gui.GUI
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalafx.application.JFXApp3
import scalafx.application.Platform
import wizard.aView.aView_TUI.TextUI
import wizard.controller.controller_TUI.IGameLogic
import wizard.controller.controller_TUI.base.GameLogic
import wizard.model.model_TUI.rounds.Game

object Wizard extends JFXApp3 {
    private val gameLogic: IGameLogic = GameLogic

    override def start(): Unit = {
        println("Welcome to Wizard!")
        // gameLogic.add(TextUI)
        val gui = new GUI()
        // gameLogic.add(gui)


        // Start the TUI thread
        Future {
            Thread.sleep(1000)
            val players = TextUI.inputPlayers()
            val game = new Game(players)
            gameLogic.playGame(game, players)
        }

        // Start the GUI thread
        Future {
            Platform.runLater {
                gui.createAndShowGUI()
            }
        }
    }
}