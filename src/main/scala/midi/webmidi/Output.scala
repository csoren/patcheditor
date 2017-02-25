package midi.webmidi

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSName, ScalaJSDefined}
import scala.scalajs.js.|


@ScalaJSDefined
class Options(time: js.UndefOr[Long] = js.undefined) extends js.Object


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

  @JSName("sendControlChange")
  private[webmidi] def _sendControlChange(controller: Int | String, value: js.UndefOr[Int] = js.undefined, channel: js.UndefOr[Int | String] = js.undefined, options: js.UndefOr[Options] = js.undefined): Output = js.native
}


object Output {
  private def asJs(time: Option[Long]): js.UndefOr[Options] =
    time.fold[js.UndefOr[Options]](js.undefined)(t => js.defined(new Options(time = t)))

  private def asJs(channel: Channel): Int | String =
    channel match {
      case All() => "all"
      case Single(n) => n
    }

  implicit final class OutputExt(val output: Output) {
    def sendControlChange(controller: Int, value: Int = 0, channel: Channel = All(), time: Option[Long] = None): Output = {
      output._sendControlChange(controller, value, asJs(channel), asJs(time))
    }
  }
}