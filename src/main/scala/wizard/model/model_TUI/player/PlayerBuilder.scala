package wizard.model.model_TUI.player

trait PlayerBuilder {
    def setName(name: String): PlayerBuilder
    def reset(): PlayerBuilder
    def build(): Player
}