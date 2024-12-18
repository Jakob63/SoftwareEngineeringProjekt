package wizard.gui

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.image.ImageView
import scalafx.scene.layout.GridPane
import wizard.model.cards.{Card, Dealer}

object CardDisplay extends JFXApp3 {

    override def start(): Unit = {
        val gridPane = new GridPane()

        // Load all cards and add them to the grid
        val cards = Dealer.allCards
        for ((card, index) <- cards.zipWithIndex) {
            val imageView = new ImageView(card.toImage) {
                fitWidth = 100
                fitHeight = 150
                preserveRatio = true
            }
            gridPane.add(imageView, index % 10, index / 10)
        }

        stage = new JFXApp3.PrimaryStage {
            title.value = "Card Display"
            width = 1200
            height = 800
            scene = new Scene {
                root = gridPane
            }
        }
    }
}