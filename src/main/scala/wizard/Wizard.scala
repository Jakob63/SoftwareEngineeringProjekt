package wizard

import wizard.gui.GUI

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalafx.application.JFXApp3
import scalafx.application.Platform
import wizard.aView.aView_TUI.TextUI
import wizard.controller.controller_TUI.{IGameLogic, IPlayerLogic, IRoundLogic}
import wizard.controller.controller_TUI.base.GameLogic
import wizard.model.model_TUI.rounds.Game
import com.google.inject.Guice

//object Wizard extends JFXApp3 {
object Wizard {
    //private val gameLogic: IGameLogic = GameLogic

    val injector = Guice.createInjector(new WizardModule)
    val gameLogic = injector.getInstance(classOf[IGameLogic])
    val playerLogic = injector.getInstance(classOf[IPlayerLogic])
    val roundLogic = injector.getInstance(classOf[IRoundLogic])


    //override def start(): Unit = {
    def main(args: Array[String]): Unit = {
        println("Welcome to Wizard!")
        // gameLogic.add(TextUI)
        //val gui = new GUI()
        // gameLogic.add(gui)

        val players = TextUI.inputPlayersTest()
        val game = new Game(players)
        gameLogic.playGame(game, players)


        // Start the TUI thread
//        Future {
//            Thread.sleep(1000)
//            val players = TextUI.inputPlayers()
//            val game = new Game(players)
//            gameLogic.playGame(game, players)
//        }

        // Start the GUI thread
//        Future {
//            Platform.runLater {
//                gui.createAndShowGUI()
//            }
//        }
    }
}