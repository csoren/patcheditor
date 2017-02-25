package ui

import com.thoughtworks.binding.{Binding, dom}
import materialize._
import midi.webmidi._
import org.scalajs.dom.Node
import org.scalajs.dom.html.Select
import rxscalajs.Observable

class Midi() {
  private class PortAndChannel[T <: midi.webmidi.Port](label: String, includeAll: Boolean) {
    private var _ports: IndexedSeq[T] = IndexedSeq.empty

    private val device = mkSelect

    private val channel = {
      val select = mkSelect
      if (includeAll)
        select.appendChild(mkOption("All", "all"))
      val options = (1 to 16).map(_.toString).map(v => mkOption(v, v))
      options.foreach(select.appendChild)
      select
    }

    def setPorts(ports: IndexedSeq[T]): Unit = {
      _ports = ports
      val options = ports.map { p => mkOption(p.name, p.id) }
      device.setMaterialOptions(options)
    }

    val selectedDevice: Observable[T] =
      device.selectedIndexObservable.map(_ports)

    val selectedChannel: Observable[Channel] =
      channel.selectedValueObservable.map {
        case "all" => All()
        case s => Single(s.toInt)
      }

    val selectedDeviceAndChannel: Observable[(T,Channel)] =
      selectedDevice.combineLatest(selectedChannel)

    @dom
    def div: Binding[Node] = {
      <div class="row">
        <div class="input-field col offset-m3 s10 m5">
          { device }
          <label>{ label }</label>
        </div>
        <div class="input-field col s2 m1">
          { channel }
          <label>Channel</label>
        </div>
      </div>
    }
  }

  private val output = new PortAndChannel[Output]("MIDI output device", includeAll = false)

  val selectedOutput: Observable[(Output,Channel)] = output.selectedDeviceAndChannel

  def setOutput(ports: IndexedSeq[Output]): Unit =
    output.setPorts(ports)

  private val input = new PortAndChannel[Input]("MIDI input device", includeAll = true)

  val selectedInput: Observable[(Input,Channel)] = input.selectedDeviceAndChannel

  def setInput(ports: IndexedSeq[Input]): Unit =
    input.setPorts(ports)

  @dom
  def layout: Binding[Node] =
    <div>
      { output.div.bind }
      { input.div.bind }
    </div>

}