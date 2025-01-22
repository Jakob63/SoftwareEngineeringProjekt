package wizard

import com.google.inject.AbstractModule
import wizard.controller.controller_TUI.*
import wizard.controller.controller_TUI.base.{GameLogic, PlayerLogic, RoundLogic}

class WizardModule extends AbstractModule {

    val defaultRounds: Int = 10

    override def configure(): Unit = {
        bind(classOf[IGameLogic]).toInstance(GameLogic)
        bind(classOf[IPlayerLogic]).toInstance(PlayerLogic)
        bind(classOf[IRoundLogic]).toInstance(RoundLogic)
    }
}