package wizard.actionmanagement

import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.{should, shouldBe}
import org.scalatest.wordspec.AnyWordSpec

class ObserverTests extends AnyWordSpec with Matchers {
    "Observer" should {

        "should add observer" in {
            val observable = new Observable()
            val observer = new Observer() {
                override def update(updateMSG: String, obj: Any*): Unit = {}
            }
            observable.add(observer)
            observable.subscribers should contain(observer)
        }
        "should remove observer" in {
            val observable = new Observable()
            val observer = new Observer(){
                override def update(updateMSG: String, obj: Any*): Unit = {}
            }
            observable.add(observer)
            observable.remove(observer)
            observable.subscribers should not contain observer
        }
        "should notify observer" in {
            val observable = new Observable()
            var notified = false
            val observer = new Observer(){
                override def update(updateMSG: String, obj: Any*): Unit = {
                    notified = true
                }
            }
            observable.add(observer)
            observable.notifyObservers("test")
            notified shouldBe true
        }

    }
}
