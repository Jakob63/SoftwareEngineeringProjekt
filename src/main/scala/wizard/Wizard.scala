package wizard

import wizard.gui.GUI
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalafx.application.JFXApp3
import scalafx.application.Platform
import wizard.aView.aView_TUI.TextUI
import wizard.controller.controller_TUI.GameLogic
import wizard.model.model_TUI.rounds.Game

object Wizard extends JFXApp3 {
    
    override def start(): Unit = {
        println("Welcome to Wizard!")
        GameLogic.add(TextUI)
        val gui = new GUI()
        GameLogic.add(gui)

        
        // Start the TUI thread
        Future {
            Thread.sleep(1000)
            println("Starting TUI thread") // Debugging
            val players = TextUI.inputPlayers()
            val game = new Game(players)
            println("Starting game") // Debugging
            GameLogic.playGame(game, players)
        }

        // Start the GUI thread
        Future {
            Platform.runLater {
                gui.createAndShowGUI()
            }
        }
    }
}