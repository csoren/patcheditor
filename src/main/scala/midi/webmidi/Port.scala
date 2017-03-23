package midi.webmidi

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.JSName


@js.native
trait Port extends js.Object {
  val id: String = js.native
  val manufacturer: String = js.native
  val name: String = js.native
}

@js.native
private[webmidi] trait PortFacade extends Port {
  @JSName("connection")
  val _connection: String = js.native
  @JSName("state")
  val _state: String = js.native
}


object Port {
  implicit final class PortExt(val port: Port) {
    private implicit def toFacade(v: Port): PortFacade = v.asInstanceOf[PortFacade]

    def connection: PortStatus.PortStatus = PortStatus.withName(port._connection)
    def state: PortState.PortState = PortState.withName(port._state)
  }
}