package midi.webmidi

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, JSName, ScalaJSDefined}
import scala.scalajs.js.|


@ScalaJSDefined
class ControlChangeOptions(time: js.UndefOr[Long] = js.undefined) extends js.Object


@ScalaJSDefined
class PlayNoteOptions(duration: js.UndefOr[Long] = js.undefined, rawVelocity: Boolean = false, release: Float = 0.5f, time: js.UndefOr[Long] = js.undefined, velocity: Float | Int = 0.5f) extends js.Object


@js.native
@JSGlobal("Output")
class Output extends Port

@js.native
private[webmidi] trait OutputFacade extends Output {
  @JSName("sendControlChange")
  def _sendControlChange(controller: Int | String, value: js.UndefOr[Int] = js.undefined, channel: js.UndefOr[Int | String] = js.undefined, options: js.UndefOr[ControlChangeOptions] = js.undefined): Output = js.native

  @JSName("playNote")
  def _playNote(note: Int | String | Array[Int], channel: js.UndefOr[Int | String] = js.undefined, options: js.UndefOr[PlayNoteOptions] = js.undefined): Output = js.native
}


object Output {
  private def asJs(time: Option[Long]): js.UndefOr[ControlChangeOptions] =
    time.fold[js.UndefOr[ControlChangeOptions]](js.undefined)(t => js.defined(new ControlChangeOptions(time = t)))

  implicit final class OutputExt(val output: Output) {
    private implicit def toFacade(v: Output): OutputFacade = v.asInstanceOf[OutputFacade]

    def sendControlChange(controller: Int, value: Int = 0, channel: Channel = All(), time: Option[Long] = None): Output =
      output._sendControlChange(controller, value, channel.asJs, asJs(time))

    def playNote(note: Int, channel: Channel = All(), velocity: Int = 96): Output =
      output._playNote(note, channel.asJs, new PlayNoteOptions(rawVelocity = true, velocity = velocity))
  }
}