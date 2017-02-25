package ui

import com.thoughtworks.binding.{Binding, dom}
import midi.webmidi.{Channel, Output, Single}
import org.scalajs.dom.Node
import org.scalajs.dom.html.Select
import rxscalajs.Observable

class Midi() {
  private var _outputPorts: IndexedSeq[Output] = IndexedSeq.empty

  private val midiOutputChannel: Select = {
    val select = mkSelect()
    val options = (1 to 16).map(_.toString).map(v => mkOption(v, v))
    options.foreach(select.appendChild)
    select
  }

  private val midiOutputDevice = mkSelect()

  @dom
  private def midiOutputDiv: Binding[Node] = {
    <div class="row">
      <div class="input-field col offset-m3 s10 m5">
        { midiOutputDevice }
        <label>MIDI output device</label>
      </div>
      <div class="input-field col s2 m1">
        { midiOutputChannel }
        <label>Channel</label>
      </div>
    </div>
  }

  def setOutput(ports: IndexedSeq[Output]): Unit = {
    _outputPorts = ports
    val options = ports.map { p => mkOption(p.name, p.id) }
    midiOutputDevice.setMaterialOptions(options)
  }

  val selectedMidiOutputDevice: Observable[Output] =
    midiOutputDevice.selectedIndexObservable.map(_outputPorts)

  val selectedMidiOutputChannel: Observable[Channel] =
    midiOutputChannel.selectedIndexObservable.map(n => Single(n+1))

  @dom
  def layout: Binding[Node] =
    <div>
      { midiOutputDiv.bind }
    </div>

}
