package wizard.undo

import wizard.model.player.Player

class SetPlayerNameCommand(player: Player, newName: String) extends Command {
    private var oldName: String = player.name
// TODO: stimmt das Janis?
    override def doStep(): Unit = {
        // player = player.changeName(newName)
        new SetPlayerNameCommand(player.changeName(newName), newName)
    }

    override def undoStep(): Unit = {
        // player = player.changeName(oldName)
        new SetPlayerNameCommand(player.changeName(oldName), oldName)
    }
}
