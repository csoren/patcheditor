package webmidi

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.JSName
import scala.scalajs.js.{Error, UndefOr, |}

@js.native
@JSName("WebMidi")
private[webmidi] object NativeWebMidi extends js.Object {
  def addListener(`type`: String, listener: js.Function1[Event, Unit]): NativeWebMidi.type = js.native

  def removeListener(`type`: String, listener: js.Function1[Event, Unit]): NativeWebMidi.type = js.native

  def hasListener(`type`: String, listener: js.Function1[Event, Unit]): Boolean = js.native

  def disable(): Unit = js.native

  def enable(callback: js.Function1[js.UndefOr[js.Error], Unit], sysex: Boolean = false): Unit = js.native

  def getInputById(id: String): Input | Boolean = js.native

  def getInputByName(name: String): Input | Boolean = js.native

  def getOutputById(id: String): Output | Boolean = js.native

  def getOutputByName(name: String): Output | Boolean = js.native

  def guessNoteNumber(input: String): Int = js.native

  def noteNameToNumber(input: String): Int = js.native

  // Properties

  def enabled: Boolean = js.native

  def sysexEnabled: Boolean = js.native

  def time: Double = js.native

  val supported: Boolean = js.native

  val inputs: js.Array[Input] = js.native

  val outputs: js.Array[Output] = js.native

  val MIDI_CHANNEL_MESSAGES: js.Dynamic = js.native

  val MIDI_CHANNEL_MODE_MESSAGES: js.Dynamic = js.native

  val MIDI_REGISTERED_PARAMETER: js.Dynamic = js.native

  val MIDI_SYSTEM_MESSAGES: js.Dynamic = js.native

}

object WebMidi {
  def addListener(`type`: PortState.PortState)(listener: js.Function1[Event,Unit]): WebMidi.type = {
    NativeWebMidi.addListener(`type`.toString, listener)
    WebMidi
  }

  def hasListener(`type`: PortState.PortState)(listener: js.Function1[Event,Unit]): Boolean =
    NativeWebMidi.hasListener(`type`.toString, listener)

  def removeListener(`type`: PortState.PortState)(listener: js.Function1[Event,Unit]): WebMidi.type = {
    NativeWebMidi.removeListener(`type`.toString, listener)
    WebMidi
  }

  val disable: () => Unit = NativeWebMidi.disable _

  def enable(sysex: Boolean)(callback: js.Function1[UndefOr[Error], Unit]): Unit =
    NativeWebMidi.enable(callback, sysex)

  private implicit def toOption[T1 : Manifest,T2](v: T1 | T2): Option[T1] =
    v match {
      case t: T1 => Some(t)
      case _ => None
     }

  def getInputById(id: String): Option[Input] =
    NativeWebMidi.getInputById(id)

  def getInputByName(name: String): Option[Input] =
    NativeWebMidi.getInputByName(name)

  def getOutputById(id: String): Option[Output] =
    NativeWebMidi.getOutputById(id)

  def getOutputByName(name: String): Option[Output] =
    NativeWebMidi.getOutputByName(name)

  val guessNoteNumber: (String) => Int = NativeWebMidi.guessNoteNumber

  val noteNameToNumber: (String) => Int = NativeWebMidi.noteNameToNumber

  def enabled: Boolean = NativeWebMidi.enabled

  def sysexEnabled: Boolean = NativeWebMidi.sysexEnabled

  def time: Double = NativeWebMidi.time

  val supported: Boolean = NativeWebMidi.supported

  val inputs: js.Array[Input] = NativeWebMidi.inputs

  val outputs: js.Array[Output] = NativeWebMidi.outputs

  val MIDI_CHANNEL_MESSAGES: js.Dynamic = NativeWebMidi.MIDI_CHANNEL_MESSAGES

  val MIDI_CHANNEL_MODE_MESSAGES: js.Dynamic = NativeWebMidi.MIDI_CHANNEL_MODE_MESSAGES

  val MIDI_REGISTERED_PARAMETER: js.Dynamic = NativeWebMidi.MIDI_REGISTERED_PARAMETER

  val MIDI_SYSTEM_MESSAGES: js.Dynamic = NativeWebMidi.MIDI_SYSTEM_MESSAGES

}
