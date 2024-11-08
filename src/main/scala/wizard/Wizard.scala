package wizard

import wizard.cards.Dealer
import wizard.cards.Value.Thirteen
import wizard.cards.Value.One
import wizard.cards.Value.WizardKarte
import wizard.cards.Color.Green
import wizard.cards.Color.Red
import wizard.cards.Color.Blue
import wizard.cards.Card
import wizard.player.Player

object Wizard {

    class Wizard {

    }

    def main(args: Array[String]): Unit = {
        println("Welcome to Wizard!")
        val card1 = Card(WizardKarte, Green)
        val card2 = Card(One, Red)
        val card3 = Card(Thirteen, Blue)
        println(card1.showcard())
        println(card2.showcard())
        println(card3.showcard())

        val players = List(Player("Player1"), Player("Player2"), Player("Player3"))
        val rest = Dealer.dealCards(3, players)
        println("fertig ausgeteilt")
        println("Trumcard:")
        println(rest.head.showcard())
        Player("Player1").showHand()
    }

    val eol = sys.props("line.separator")
    def bar(cellWidth: Int = 4, cellNum: Int = 2) =
        (("-" * cellWidth) * cellNum) + "-" + eol

    def bar2(cellWidth: Int = 16, cellNum: Int = 2) =
        (("-" * cellWidth) * cellNum) + "-" + eol

    def cells(cellWidth: Int = 7, cellNum: Int = 1) =
        ("|" + " " * cellWidth) * cellNum + "|" + eol

    def cells2() =
        "|" + " game  " + "|" + eol

    def cells3() =
        "|" + " trump " + "|" + eol

    def cells4() =
        ("|" + "Set win" + "|" + "\t") * 3 + eol

    def cells5(cellWidth: Int = 7, cellNum: Int = 1) =
        (("|" + " " * cellWidth) * cellNum + "|" + "\t") * 3 + eol


    //def mesh =
    //    (bar() + cells() * 3) + bar()

    def mesh2: String =
        bar() + cells() + cells2() + cells() + bar()

    def mesh3: String =
        bar() + cells() + cells3() + cells() + bar()

    def mesh4: String =
        bar2() + cells5() + cells4() + cells5() + bar2()

    println(mesh2)
    println(mesh3)
    println(mesh4)


}