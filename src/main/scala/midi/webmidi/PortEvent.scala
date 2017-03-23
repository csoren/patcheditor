package midi.webmidi

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
trait PortEvent extends js.Object {
  val timestamp: Long = js.native
  val id: String = js.native
  val manufacturer: String = js.native
  val name: String = js.native
  val output: js.UndefOr[Output] = js.native
  val input: js.UndefOr[Input] = js.native
}

@js.native
private[webmidi] trait PortEventFacade extends PortEvent {
  @JSName("type")
  val _type: String = js.native
}


object PortEvent {
  implicit final class PortEventExt(val event: PortEvent) {
    private implicit def toFacade(v: PortEvent): PortEventFacade = v.asInstanceOf[PortEventFacade]
    
    def eventType: PortState.PortState = PortState.withName(event._type)
  }
}