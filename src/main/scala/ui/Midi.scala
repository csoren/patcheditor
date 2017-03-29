package ui

import midi.webmidi._
import rxscalajs.Observable

import scala.scalajs.js
import reactive._

class Midi(ui: MyMidi) {
  private class PortAndChannel[T <: midi.webmidi.Port](uiSetPorts: js.Array[String] => Unit, uiPort: Observable[js.UndefOr[Int]], uiChannel: Observable[js.UndefOr[Int]]) {
    private var _ports: IndexedSeq[T] = IndexedSeq.empty

    def setPorts(ports: IndexedSeq[T]): Unit = {
      _ports = ports
      uiSetPorts(js.Array(ports.map(_.name) :_*))
    }

    def selectedDevice: Observable[T] =
      uiPort.map(_.toOption).flattenOption.map(v => _ports(v))

    def selectedChannel: Observable[Channel] =
      uiChannel.map(_.toOption).map {
        case Some(n) if n >= 0 && n <= 15 => Single(n + 1)
        case _ => All()
      }

    def selectedDeviceAndChannel: Observable[(T,Channel)] =
      selectedDevice.combineLatest(selectedChannel)
  }

  private val output = new PortAndChannel[Output](v => ui.outputPortItems = v, ui.outputPortObservable, ui.outputChannelObservable)

  def setOutput(ports: IndexedSeq[Output]): Unit = output.setPorts(ports)

  val selectedOutput: Observable[(Output,Channel)] = output.selectedDeviceAndChannel

  private val input = new PortAndChannel[Input](v => ui.inputPortItems = v, ui.inputPortObservable, ui.inputChannelObservable)

  def setInput(ports: IndexedSeq[Input]): Unit = input.setPorts(ports)

  val selectedInput: Observable[(Input,Channel)] = input.selectedDeviceAndChannel


}
