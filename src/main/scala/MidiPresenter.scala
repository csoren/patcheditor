import midi.webmidi.{Channel, Output, WebMidi}
import rxscalajs.Observable
import ui.Midi

class MidiPresenter(view: Midi) {
  private val midiPortsChangedObservable =
    midi.enabledObservable.map(_ => ())
      .merge(midi.connectedObservable.map(_ => ()))
      .merge(midi.disconnectedObservable.map(_ => ()))

  private val midiPortsObservable =
    midiPortsChangedObservable.map(_ => WebMidi.outputs)

  midiPortsObservable.subscribe(view.setOutput(_))

  val selectedMidiOutput: Observable[(Output,Channel)] =
    view.selectedMidiOutputDevice.combineLatest(view.selectedMidiOutputChannel)

}
