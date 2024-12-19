// File: src/main/scala/wizard/aView/aView_GUI/StartNodes.scala
package wizard.aView.aView_GUI

import javafx.scene.layout.VBox
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.collections.CollectionIncludes.observableList2ObservableBuffer

import scala.jdk.CollectionConverters.*
import scalafx.Includes.*

import scala.collection.mutable.{ListBuffer, Stack}
import scalafx.scene.layout.HBox
import wizard.aView.aView_TUI.TextUI
import wizard.actionmanagement.{Observer, Observable}
import wizard.model.model_TUI.player.Human
import wizard.model.model_TUI.player.PlayerFactory
import wizard.model.model_TUI.rounds.Game
import wizard.model.model_TUI.player.PlayerType

object StartNodes extends Observer {
  var textField: TextField = new TextField()
  var messageLabel: Label = new Label()
  private var nameFields: ListBuffer[TextField] = ListBuffer()
  private val undoStack: Stack[List[String]] = Stack()
  private val redoStack: Stack[List[String]] = Stack()
  private var numPlayers: Int = 0
  private var currentPlayerIndex: Int = 0
  private var playerNames: ListBuffer[String] = ListBuffer()

  override def update(updateMSG: String, obj: Any*): Unit = {
    println(s"StartNodes received update: $updateMSG with data: $obj") // Debugging
    updateMSG match {
      case "setNumPlayersGUI" =>
        val numberOfPlayers = obj.head.asInstanceOf[Int]
        println(s"Number of players set in StartNodes: $numberOfPlayers") // Debugging
        numPlayers = numberOfPlayers
        println("test zugewiesen") // Debugging
        createNameFields(numPlayers)
        println("test createNameFields") // Debugging
      case _ =>
        println(s"Unknown update message in StartNodes: $updateMSG")
    }
  }

  def createTextField(root: javafx.scene.layout.VBox, submitAction: () => Unit): Unit = {
    textField.promptText = "Enter the number of players (3-6)"
    val submitButton = new Button("Submit") {
      onAction = (_: ActionEvent) => submitAction()
    }
    val playerCountLabel = new Label("Spieleranzahl:")
    Platform.runLater {
      root.getChildren.headOption.foreach(_.setVisible(false))
      root.getChildren.addAll(playerCountLabel, textField, submitButton, messageLabel)
    }
  }

  def submitInput(): Unit = {
    val input = textField.text.value
    val numPlayers = input.toIntOption.getOrElse(-1)
    if (numPlayers >= 3 && numPlayers <= 6) {
      messageLabel.text = "" // Clear error message
      TextUI.notifyAllObservers("setNumPlayersTUI", Seq(numPlayers)) // Notify the TUI with the number of players
      createNameFields(numPlayers)
    } else {
      messageLabel.text = "Invalid number of players. Please enter a number between 3 and 6."
    }
  }

  private def askForNextPlayerName(): Unit = {
    if (currentPlayerIndex < numPlayers) {
      Platform.runLater {
        val root = textField.getParent.asInstanceOf[javafx.scene.layout.VBox]
        root.getChildren.clear()
        val nameField = new TextField() {
          promptText = s"Enter name for Player ${currentPlayerIndex + 1}"
        }
        nameFields.clear()
        nameFields += nameField
        val submitButton = new Button("Submit") {
          onAction = (_: ActionEvent) => submitPlayerName(nameField.text.value)
        }
        root.getChildren.addAll(nameField, submitButton, messageLabel)
      }
    } else {
      startGame()
    }
  }

  private def submitPlayerName(name: String): Unit = {
    if (name.nonEmpty && !playerNames.contains(name)) {
      playerNames += name
      currentPlayerIndex += 1
      askForNextPlayerName()
    } else {
      messageLabel.text = "Invalid or duplicate name. Please enter a unique name."
    }
  }

  private def validateNames(): Boolean = {
    val names = nameFields.map(_.text.value).toList
    if (names.contains("") || names.distinct.size != names.size) {
      messageLabel.text = "Please fill in all fields with unique names."
      false
    } else {
      true
    }
  }

  private def createNameFields(numPlayers: Int): Unit = {
    nameFields.clear()
    val root = textField.getParent.asInstanceOf[javafx.scene.layout.VBox]
    Platform.runLater {
      root.getChildren.clear()
      for (i <- 1 to numPlayers) {
        val nameField = new TextField() {
          promptText = s"Enter name for Player $i"
        }
        nameFields += nameField
        root.getChildren.add(nameField)
      }

      val buttonBox = new HBox {
        spacing = 10
        children = Seq(
          new Button("Start Game") {
            onAction = (_: ActionEvent) => startGame()
          },
          new Button("Reset") {
            onAction = (_: ActionEvent) => resetNameFields(numPlayers)
          },
          new Button("Undo") {
            onAction = (_: ActionEvent) => undoReset()
          },
          new Button("Redo") {
            onAction = (_: ActionEvent) => redoReset()
          }
        )
      }

      root.getChildren.addAll(buttonBox, messageLabel)
    }
  }

  private def resetNameFields(numPlayers: Int): Unit = {
    undoStack.push(nameFields.map(_.text.value).toList)
    redoStack.clear()
    nameFields.foreach(_.text = "")
  }

  private def undoReset(): Unit = {
    if (undoStack.nonEmpty) {
      val previousState = undoStack.pop()
      redoStack.push(nameFields.map(_.text.value).toList)
      nameFields.zip(previousState).foreach { case (field, text) => field.text = text }
    }
  }

  private def redoReset(): Unit = {
    if (redoStack.nonEmpty) {
      val nextState = redoStack.pop()
      undoStack.push(nameFields.map(_.text.value).toList)
      nameFields.zip(nextState).foreach { case (field, text) => field.text = text }
    }
  }

  private def startGame(): Unit = {
    if (playerNames.size == numPlayers) {
      // Proceed with starting the game

      // Initialize the game with player names
      val players = playerNames.map(name => PlayerFactory.createPlayer(Some(name), PlayerType.Human)).toList
      val game = new Game(players)

      // Transition to the game view
      val root = textField.getParent.asInstanceOf[javafx.scene.layout.VBox]
      root.getChildren.clear()
      val gameView = new VBox()
      gameView.setSpacing(10)
      gameView.getChildren.addAll(
        new Label("Game started with players:"),
        new Label(playerNames.mkString(", "))
      )
      root.getChildren.add(gameView)
    }
  }
}