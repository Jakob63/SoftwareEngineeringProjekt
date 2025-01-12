package wizard.undo

trait Command {
  def doStep(): Unit
  def undoStep(): Unit
  def redoStep(): Unit
}