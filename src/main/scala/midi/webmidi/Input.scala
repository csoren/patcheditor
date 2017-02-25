package midi.webmidi

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("Input")
class Input extends js.Object {
  val id: String = js.native
  val manufacturer: String = js.native
  val name: String = js.native

  @JSName("connection")
  private[webmidi] val _connection: String = js.native
  @JSName("state")
  private[webmidi] val _state: String = js.native
}


object Input {
  implicit final class InputExt(val input: Input) {
    def connection: PortStatus.PortStatus = PortStatus.withName(input._connection)
    def state: PortState.PortState = PortState.withName(input._state)
  }
}