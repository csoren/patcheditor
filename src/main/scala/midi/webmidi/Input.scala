package midi.webmidi

import rxscalajs.Observable

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, JSName}
import scala.scalajs.js.|

@js.native
@JSGlobal("Input")
class Input extends Port

@js.native
private[webmidi] trait InputFacade extends Input {
  @JSName("addListener")
  def _addListener(`type`: String, channel: Int | Array[Int] | String, callback: js.Function1[_ <: InputEvent,_]): Input = js.native

  @JSName("removeListener")
  def _removeListener(`type`: js.UndefOr[String], channel: js.UndefOr[Int | Array[Int] | String], callback: js.UndefOr[js.Function1[_ <: InputEvent,_]]): Input = js.native
}


object Input {
  implicit final class InputExt(val input: Input) extends AnyVal {
    private implicit def toFacade(v: Input): InputFacade = v.asInstanceOf[InputFacade]

    def addListener(`type`: InputEventName, channel: Channel)(callback: js.Function1[`type`.EventType, _]): Input = {
      input._addListener(`type`.asInstanceOf[String], channel.asJs, callback)
    }

    def removeListener(`type`: InputEventName, channel: Channel)(callback: js.Function1[`type`.EventType, _]): Input = {
      input._removeListener(`type`.asInstanceOf[String], channel.asJs, callback)
    }

    def observableOf(`type`: InputEventName, channel: Channel): Observable[`type`.EventType] =
      Observable.create[`type`.EventType] { observer =>
        val callback: js.Function1[`type`.EventType, _] = observer.next
        addListener(`type`, channel)(callback)
        () => {
          removeListener(`type`, channel)(callback)
          ()
        }
      }

  }
}