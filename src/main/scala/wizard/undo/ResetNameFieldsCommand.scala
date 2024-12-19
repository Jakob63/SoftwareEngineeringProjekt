package wizard.undo

import scalafx.scene.control.TextField
import scala.collection.mutable.ListBuffer

class ResetNameFieldsCommand(nameFields: ListBuffer[TextField]) extends Command {
  private val previousState: List[String] = nameFields.map(_.text.value).toList

  override def doStep(): Unit = {
    nameFields.foreach(_.clear())
  }

  override def undoStep(): Unit = {
    for ((field, text) <- nameFields.zip(previousState)) {
      field.text = text
    }
  }

  override def redoStep(): Unit = {
    doStep()
  }
}