package webmidi

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("Output")
class Output extends js.Object {
  val id: String = js.native
  val manufacturer: String = js.native
  val name: String = js.native

  @JSName("connection")
  private[webmidi] val _connection: String = js.native
  @JSName("state")
  private[webmidi] val _state: String = js.native
}

