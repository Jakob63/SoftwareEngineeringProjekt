package wizard.gui

import scalafx.application.JFXApp3
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color as JFXColor
import wizard.model.player.{Player, PlayerFactory}
import wizard.model.player.PlayerType.Human
import wizard.model.rounds.Game
import wizard.controller.{GameLogic, RoundLogic, GUIPlayerLogic}
import wizard.model.cards.{Card, Dealer}
import wizard.actionmanagement.Observer

object WizardApp extends JFXApp3 with Observer {

    var players: List[Player] = List()
    var game: Game = _
    var currentRound: Int = 1
    var playerCount: Int = 0
    var currentPlayerIndex: Int = 0

    override def start(): Unit = {
        stage = new JFXApp3.PrimaryStage {
            title.value = "Wizard Game"
            width = 800
            height = 600
            scene = new Scene {
                fill = JFXColor.White
                root = new VBox {
                    spacing = 10
                    alignment = Pos.Center
                    style = "-fx-background-color: white;"
                    children = Seq(
                        new Label("Welcome to Wizard!"),
                        new Button("Start Game") {
                            onAction = _ => showPlayerCountInput()
                        }
                    )
                }
            }
        }
    }

    def showPlayerCountInput(): Unit = {
        val playerCountInput = new TextField {
            promptText = "Enter number of players"
        }
        val submitButton = new Button("Submit") {
            onAction = _ => {
                playerCount = playerCountInput.text.value.toInt
                players = List.fill(playerCount)(null)
                currentPlayerIndex = 0
                showPlayerNameInput()
            }
        }

        stage.scene = new Scene {
            fill = JFXColor.White
            root = new VBox {
                spacing = 10
                alignment = Pos.Center
                style = "-fx-background-color: white;"
                children = Seq(
                    new Label("Enter number of players:") {
                        style = "-fx-text-fill: black;"
                    },
                    playerCountInput,
                    submitButton
                )
            }
        }
    }

    def showPlayerNameInput(): Unit = {
        val playerNameInput = new TextField {
            promptText = s"Enter name for player ${currentPlayerIndex + 1}"
        }
        val submitButton = new Button("Submit") {
            onAction = _ => {
                val playerName = playerNameInput.text.value
                if (currentPlayerIndex < playerCount) {
                    players = players.updated(currentPlayerIndex, PlayerFactory.createPlayer(Some(playerName), Human))
                    currentPlayerIndex += 1
                    if (currentPlayerIndex < playerCount) {
                        showPlayerNameInput()
                    } else {
                        startGame()
                    }
                }
            }
        }

        stage.scene = new Scene {
            fill = JFXColor.White
            root = new VBox {
                spacing = 10
                alignment = Pos.Center
                style = "-fx-background-color: white;"
                children = Seq(
                    new Label(s"Enter name for player ${currentPlayerIndex + 1}:") {
                        style = "-fx-text-fill: black;"
                    },
                    playerNameInput,
                    submitButton
                )
            }
        }
    }

    def startGame(): Unit = {
        game = new Game(players)
        startRound()
    }

    def startRound(): Unit = {
        RoundLogic.playRound(currentRound, players)
        showBidsInput()
    }

    def showBidsInput(): Unit = {
        if (currentPlayerIndex < playerCount) {
            val player = players(currentPlayerIndex)
            val bidInput = new TextField {
                promptText = s"${player.name}, enter your bid"
            }
            val submitButton = new Button("Submit") {
                onAction = _ => {
                    val bid = bidInput.text.value.toInt
                    player.roundBids = bid
                    currentPlayerIndex += 1
                    showBidsInput()
                }
            }

            stage.scene = new Scene {
                fill = JFXColor.White
                root = new VBox {
                    spacing = 10
                    alignment = Pos.Center
                    style = "-fx-background-color: white;"
                    children = Seq(
                        new Label(s"${player.name}, enter your bid:") {
                            style = "-fx-text-fill: black;"
                        },
                        bidInput,
                        submitButton
                    )
                }
            }
        } else {
            currentPlayerIndex = 0
            playRound()
        }
    }

    def playRound(): Unit = {
        if (currentPlayerIndex < playerCount) {
            val player = players(currentPlayerIndex)
            val hand = player.hand.cards.map(card => new ImageView(card.toImage) {
                fitWidth = 100
                fitHeight = 150
                preserveRatio = true
            })

            val handBox = new HBox {
                spacing = 10
                alignment = Pos.Center
                children = hand
            }

            val playButton = new Button("Play Card") {
                onAction = _ => {
                    // Implement the logic to play a card
                    currentPlayerIndex += 1
                    playRound()
                }
            }

            stage.scene = new Scene {
                fill = JFXColor.White
                root = new VBox {
                    spacing = 10
                    alignment = Pos.Center
                    style = "-fx-background-color: white;"
                    children = Seq(
                        new Label(s"${player.name}'s turn to play a card:") {
                            style = "-fx-text-fill: black;"
                        },
                        handBox,
                        playButton
                    )
                }
            }
        } else {
            currentPlayerIndex = 0
            if (currentRound < game.rounds) {
                currentRound += 1
                startRound()
            } else {
                showGameResults()
            }
        }
    }

    def showGameResults(): Unit = {
        val results = players.map(player => s"${player.name}: ${player.points} points").mkString("\n")
        stage.scene = new Scene {
            fill = JFXColor.White
            root = new VBox {
                spacing = 10
                alignment = Pos.Center
                style = "-fx-background-color: white;"
                children = Seq(
                    new Label("Game Over!") {
                        style = "-fx-text-fill: black;"
                    },
                    new Label(results) {
                        style = "-fx-text-fill: black;"
                    }
                )
            }
        }
    }

    override def update(updateMSG: String, obj: Any*): Any = {
        // Implement the update logic for the observer
    }
}