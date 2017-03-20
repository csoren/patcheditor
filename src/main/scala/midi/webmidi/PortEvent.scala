package midi.webmidi

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
trait PortEvent extends js.Object {
  @JSName("type")
  private[webmidi] val _type: String = js.native

  val timestamp: Long = js.native
  val id: String = js.native
  val manufacturer: String = js.native
  val name: String = js.native
  val output: js.UndefOr[Output] = js.native
  val input: js.UndefOr[Input] = js.native
}


object PortEvent {
  implicit final class PortEventExt(val event: PortEvent) {
    def eventType: PortState.PortState = PortState.withName(event._type)
  }
}