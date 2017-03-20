package midi.webmidi

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSName, ScalaJSDefined}
import scala.scalajs.js.|


@ScalaJSDefined
class Options(time: js.UndefOr[Long] = js.undefined) extends js.Object


@js.native
@JSName("Output")
class Output extends Port {
  @JSName("sendControlChange")
  private[webmidi] def _sendControlChange(controller: Int | String, value: js.UndefOr[Int] = js.undefined, channel: js.UndefOr[Int | String] = js.undefined, options: js.UndefOr[Options] = js.undefined): Output = js.native
}


object Output {
  private def asJs(time: Option[Long]): js.UndefOr[Options] =
    time.fold[js.UndefOr[Options]](js.undefined)(t => js.defined(new Options(time = t)))

  implicit final class OutputExt(val output: Output) {
    def sendControlChange(controller: Int, value: Int = 0, channel: Channel = All(), time: Option[Long] = None): Output = {
      output._sendControlChange(controller, value, channel.asJs, asJs(time))
    }
  }
}