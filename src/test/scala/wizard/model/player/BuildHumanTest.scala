package wizard.model.player

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import wizard.model.model_TUI.player.{AI, BuildAI, BuildHuman, Human}

class BuildHumanTest extends AnyWordSpec with Matchers {
    "BuildHuman" should {
        "set the name correctly" in {
            val builder = new BuildHuman()
            builder.setName("TestName")
            val human = builder.build().asInstanceOf[Human]
            human.name shouldBe "TestName"
        }

        "reset the builder" in {
            val builder = new BuildHuman()
            builder.setName("TestName")
            builder.reset()
            assertThrows[Exception] {
                builder.build()
            }
        }

        "throw an exception if build is called before setting a name" in {
            val builder = new BuildHuman()
            assertThrows[Exception] {
                builder.build()
            }
        }

        "allow setting the name after reset" in {
            val builder = new BuildHuman()
            builder.setName("TestName")
            builder.reset()
            builder.setName("NewName")
            val human = builder.build().asInstanceOf[Human]
            human.name shouldBe "NewName"
        }

        "update the name if unfinished player is already set" in {
            val builder = new BuildHuman()
            builder.setName("InitialName")
            builder.setName("UpdatedName")
            val human = builder.build().asInstanceOf[Human]
            human.name shouldBe "UpdatedName"
        }
        "update the name if unfinished AI is already set" in {
            val builder = new BuildAI()
            builder.setName("InitialName")
            builder.setName("UpdatedName")
            val AI = builder.build().asInstanceOf[AI]
            AI.name shouldBe "UpdatedName"
        }
    }
}