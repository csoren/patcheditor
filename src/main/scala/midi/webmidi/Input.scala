package midi.webmidi

import rxscalajs.Observable

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName
import scala.scalajs.js.|

@js.native
@JSName("Input")
class Input extends Port {
  @JSName("addListener")
  private[webmidi] def _addListener(`type`: String, channel: Int | Array[Int] | String, callback: js.Function1[_ <: InputEvent,_]): Input = js.native

  @JSName("removeListener")
  private[webmidi] def _removeListener(`type`: js.UndefOr[String], channel: js.UndefOr[Int | Array[Int] | String], callback: js.UndefOr[js.Function1[_ <: InputEvent,_]]): Input = js.native
}


object Input {
  implicit final class InputExt(val input: Input) extends AnyVal {
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