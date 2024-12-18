// src/main/scala/wizard/gui/WizardApp.scala
package wizard.gui

import scalafx.application.JFXApp3
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, ScrollPane, TextField}
import scalafx.scene.image.ImageView
import scalafx.scene.input.KeyCombination.ModifierValue.Any
import scalafx.scene.input.ScrollEvent
import scalafx.scene.layout.{GridPane, VBox}
import scalafx.scene.paint.Color as JFXColor
import wizard.model.cards.{Card, Color, Dealer, Value}
import wizard.model.player.{Player, PlayerFactory}
import wizard.model.rounds.Round
import wizard.model.player.PlayerType.Human
import wizard.controller.PlayerLogic

object WizardApp extends JFXApp3 {

    var players: List[Player] = List()
    var zoomFactor: Double = 1.0

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
                        new Label("Welcome to Wizard!") {
                            style = "-fx-text-fill: black;"
                        },
                        new Button("Start Game") {
                            style = "-fx-text-fill: black;"
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
                val playerCount = playerCountInput.text.value.toInt
                showPlayerInput(playerCount)
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

    def showPlayerInput(playerCount: Int): Unit = {
        val playerInputs = (1 to playerCount).map { i =>
            new TextField {
                promptText = s"Enter name for player $i"
            }
        }
        val submitButton = new Button("Submit") {
            onAction = _ => {
                players = playerInputs.map(input => {
                    val player = PlayerFactory.createPlayer(Some(input.text.value), Human)
                    player.name = input.text.value
                    player
                }).toList
                startFirstRound()
            }
        }

        stage.scene = new Scene {
            fill = JFXColor.White
            root = new VBox {
                spacing = 10
                alignment = Pos.Center
                style = "-fx-background-color: white;"
                children = playerInputs :+ submitButton
            }
        }
    }

    def startFirstRound(): Unit
    =
    {
        Dealer.shuffleCards()
        val round = new Round(players)
        val trumpCard = Dealer.dealCards(1).cards.head
        round.setTrump(Some(trumpCard.color))

        // Deal one card to each player
        players.foreach { player =>
            player.hand = Dealer.dealCards(1) // Each player gets 1 card
        }

        // Create a GridPane for the layout
        val gridPane = new GridPane {
            hgap = 10
            vgap = 10
            padding = Insets(10)
        }

        // Display the trump card at the top center
        val trumpCardImage = new ImageView(trumpCard.toImage) {
            fitWidth = 100
            fitHeight = 150
            preserveRatio = true
        }
        gridPane.add(new VBox {
            alignment = Pos.Center
            children = Seq(
                new Label("Trump Card:") {
                    style = "-fx-text-fill: black;"
                },
                trumpCardImage
            )
        }, 1, 0)

        // Display each player's hand from right to left with bid input
        val bidsInputs = players.zipWithIndex.map { case (player, index) =>
            val cardImage = new ImageView(player.hand.cards.head.toImage) {
                fitWidth = 100
                fitHeight = 150
                preserveRatio = true
            }
            val bidsInput = new TextField {
                promptText = s"${player.name}, enter your bid"
            }
            val playerBox = new VBox {
                spacing = 10
                alignment = Pos.Center
                children = Seq(
                    new Label(s"${player.name}'s hand:") {
                        style = "-fx-text-fill: black;"
                    },
                    cardImage,
                    bidsInput
                )
            }
            gridPane.add(playerBox, players.length - 1 - index, 1)
            bidsInput
        }

        val submitButton = new Button("Submit Bids") {
            onAction = _ => {
                val bids = bidsInputs.map(_.text.value.toInt)
                players.zip(bids).foreach { case (player, bid) =>
                    player.roundBids = bid
                }
                promptPlayerToPlayCard(0)
            }
        }

        val scrollPane = new ScrollPane {
            content = gridPane
            fitToWidth = true
            fitToHeight = true
        }

        stage.scene = new Scene {
            fill = JFXColor.White
            root = new VBox {
                spacing = 10
                alignment = Pos.Center
                style = "-fx-background-color: white;"
                children = Seq(
                    scrollPane,
                    submitButton
                )
            }
        }
    }

    def promptPlayerToPlayCard(playerIndex: Int): Unit = {
        if (playerIndex >= players.length) {
            // All players have played their cards, proceed to the next phase
            return
        }

        val player = players(playerIndex)
        val gridPane = new GridPane {
            hgap = 10
            vgap = 10
            padding = Insets(10)
        }

        val cardImages = player.hand.cards.map { card =>
            new ImageView(card.toImage) {
                fitWidth = 100
                fitHeight = 150
                preserveRatio = true
                onMouseClicked = _ => {
                    val leadColor = Color.Green // set lead color to green
                    val trump = Color.Red // set trump color to red
                    PlayerLogic.playCard(Some(leadColor), Some(trump), playerIndex, player)
                    promptPlayerToPlayCard(playerIndex + 1)
                }
            }
        }

        gridPane.add(new VBox {
            alignment = Pos.Center
            children = Seq(
                new Label(s"${player.name}, select a card to play:") {
                    style = "-fx-text-fill: black;"
                }
            ) ++ cardImages
        }, 1, 0)

        stage.scene = new Scene {
            fill = JFXColor.White
            root = new VBox {
                spacing = 10
                alignment = Pos.Center
                style = "-fx-background-color: white;"
                children = Seq(
                    gridPane
                )
            }
        }
    }
}