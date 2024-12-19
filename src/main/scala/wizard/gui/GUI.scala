// Datei: src/main/scala/wizard/gui/GUI.scala
package wizard.gui

import scalafx.application.JFXApp3
import scalafx.geometry.Pos.Center
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.{HBox, VBox}
import wizard.actionmanagement.Observer
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.event.ActionEvent
import wizard.model.model_TUI.player.PlayerType.Human
import scala.compiletime.uninitialized
import scala.collection.mutable.ListBuffer
import wizard.undo.{UndoManager, ResetNameFieldsCommand}
import scalafx.scene.paint.Color as JFXColor
import wizard.aView.aView_GUI.StartNodes
import wizard.controller.controller_TUI.GameLogic
import wizard.model.model_TUI.player.{Player, PlayerFactory}
import wizard.model.model_TUI.rounds.Game
import wizard.aView.aView_GUI.StartNodes

class GUI extends JFXApp3 with Observer {
  private val undoManager = new UndoManager
  private val textFieldNode = StartNodes

  override def update(updateMSG: String, obj: Any*): Any = {
    updateMSG match {
      case "create textfield" =>
        val root = stage.scene().root.value.delegate
        if (root.isInstanceOf[javafx.scene.layout.VBox]) {
          textFieldNode.createTextField(root.asInstanceOf[javafx.scene.layout.VBox], textFieldNode.submitInput)
        } else {
          println("Root is not a VBox")
        }
      case "setNumPlayers" => // ignore
      case _ => println(s"GUI: Unknown update message: $updateMSG")
    }
  }
  def createAndShowGUI(): Unit = {
    start()
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Wizard"
      width = 600
      height = 400
      scene = new Scene {
        fill = JFXColor.White
        root = new VBox {
          spacing = 20
          alignment = Center
          style = "-fx-background-color: black"
          style = "-fx-font-size: 20pt"
          val startButton: Button = new Button("Start") {
            onAction = (_: ActionEvent) => {
              textFieldNode.createTextField(root.value.asInstanceOf[javafx.scene.layout.VBox], textFieldNode.submitInput)
              this.setVisible(false)
            }
          }
          children = Seq(
            new Label("Welcome to Wizard!") {
              style = "-fx-text-fill: black"
            },
            startButton
          )
        }
      }
    }
  }
}