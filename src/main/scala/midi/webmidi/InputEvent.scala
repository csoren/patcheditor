package midi.webmidi

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName
import scala.scalajs.js.typedarray.Uint8Array


@js.native
trait Note extends js.Object {
  val number: Int = js.native
  val name: String = js.native
  val octave: Int = js.native
}

@js.native
trait InputEvent extends js.Object {
  val target: Input = js.native
  val data: Uint8Array = js.native
  val receivedTime: Long = js.native
  val timestamp: Long = js.native
  val channel: Int = js.native
}

@js.native
private[webmidi] trait InputEventFacade extends InputEvent {
  @JSName("type")
  val _type: String = js.native
}


@js.native
trait NoteOnEvent extends InputEvent {
  val note: Note = js.native
  val velocity: Float = js.native
  val rawVelocity: Int = js.native
}


@js.native
trait InputEventName extends js.Object {
  type EventType <: InputEvent
}


object InputEventName {
  def apply[T <: InputEvent](name: String): InputEventName { type EventType = T } =
    name.asInstanceOf[InputEventName { type EventType = T }]

  val noteon = apply[NoteOnEvent]("noteon")
}
