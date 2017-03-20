import midi.webmidi.{Channel, Input, Output, WebMidi}
import rxscalajs.Observable
import ui.Midi

class MidiPresenter(view: Midi) {
  private val midiPortsChangedObservable =
    midi.enabledObservable.map(_ => ())
      .merge(midi.connectedObservable.map(_ => ()))
      .merge(midi.disconnectedObservable.map(_ => ()))

  private val midiOutputPortsObservable =
    midiPortsChangedObservable.map(_ => WebMidi.outputs)

  private val midiInputPortsObservable =
    midiPortsChangedObservable.map(_ => WebMidi.inputs)

  val selectedMidiOutput: Observable[(Output,Channel)] =
    view.selectedOutput

  val selectedMidiInput: Observable[(Input,Channel)] =
    view.selectedInput

  midiOutputPortsObservable.subscribe(view.setOutput(_))

  midiInputPortsObservable.subscribe(view.setInput(_))

}
