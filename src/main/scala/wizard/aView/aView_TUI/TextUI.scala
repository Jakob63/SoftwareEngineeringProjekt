// File: src/main/scala/wizard/aView/aView_TUI/TextUI.scala
package wizard.aView.aView_TUI

import wizard.actionmanagement.{Observable, Observer}
import wizard.controller.controller_TUI.GameLogic
import wizard.model.model_TUI.player.{Player, PlayerFactory}
import wizard.model.model_TUI.player.PlayerType.Human
import wizard.undo.{SetPlayerNameCommand, UndoManager}
import wizard.aView.aView_GUI.StartNodes
import wizard.aView.aView_TUI.TextUI.observers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.concurrent.{Await, Future}
import scala.io.StdIn
import scala.util.{Success, Try}

object TextUI extends Observable with Observer {
    private val undoManager = new UndoManager
    @volatile private var guiPlayerNames: Option[List[String]] = None
    private var numPlayers: Option[Int] = None
    private var observers: List[Observer] = List(this)

    // startnodes als observer
    addObserver(StartNodes)

    override def update(updateMSG: String, obj: Any*): Unit = {
        updateMSG match {
            case "setNumPlayersTUI" =>
                numPlayers = Some(obj.head.asInstanceOf[Int])
                println(s"Number of players set in TextUI: ${numPlayers.get}") // Debugging
            case _ =>
                println(s"Unknown update message: $updateMSG")
        }
    }

    def showHand(player: Player): Unit = {
        println(s"${player.name}'s hand: ${player.hand.toString}")
    }

    def setPlayerNamesFromGUI(names: List[String]): Unit = {
        println(s"Setting player names from GUI: $names") // Debugging
        guiPlayerNames = Some(names)
    }

    def readLineWithTimeout(timeout: Duration): String = {
        val inputFuture = Future {
            StdIn.readLine()
        }
        Try(Await.result(inputFuture, timeout)).getOrElse("")
    }

    def inputPlayers(): List[Player] = {
        print("Enter the number of players (3-6): ")
        while (numPlayers.isEmpty) {
            val input = readLineWithTimeout(3.seconds)
            println(s"Input received: '$input'") // Debugging
            if (input.nonEmpty) {
                numPlayers = Try(input.toInt) match {
                    case Success(number) if number >= 3 && number <= 6 =>
                        notifyAllObservers("setNumPlayersGUI", Seq(number))
                        println(s"Valid number of players: $number")
                        Some(number)
                    case _ =>
                        println("Invalid number of players. Please enter a number between 3 and 6.")
                        None
                }
            }
        }

        println(s"Number of players set to: ${numPlayers.get}")

        var players = List[Player]()
        var i = 1
        while (i <= numPlayers.get) {
            if (guiPlayerNames.isDefined) {
                println("Using player names from GUI")
                val names = guiPlayerNames.get
                players = names.map(name => PlayerFactory.createPlayer(Some(name), Human))
                return players
            }

            var name: Option[String] = None
            val pattern = "^[a-zA-Z0-9]+$".r
            while (name.isEmpty || !pattern.pattern.matcher(name.getOrElse("")).matches()) {
                val input = readLineWithTimeout(5.seconds)
                input match {
                    case "undo" =>
                        if (i > 1) {
                            undoManager.undoStep()
                            i -= 1
                            players = players.dropRight(1)
                        }
                    case "redo" =>
                        undoManager.redoStep()
                        if (i <= players.length) {
                            players = players :+ players(i - 1)
                        }
                        if (i < numPlayers.get) {
                            i += 1
                        }
                    case _ =>
                        if (input == "" || !pattern.pattern.matcher(input).matches()) {
                            println("Invalid name. Please enter a name containing only letters and numbers.")
                        } else {
                            name = Some(input)
                            val player = PlayerFactory.createPlayer(name, Human)
                            undoManager.doStep(new SetPlayerNameCommand(player, input))
                            players = players :+ player
                            i += 1
                        }
                }
            }
        }
        players
    }

    def addObserver(observer: Observer): Unit = {
        observers = observers :+ observer
    }

    def notifyAllObservers(message: String): Unit = {
        observers.foreach(_.update(message))
    }

    def notifyAllObservers(message: String, data: Seq[Any]): Unit = {
        observers.foreach(_.update(message, data: _*))
    }
}