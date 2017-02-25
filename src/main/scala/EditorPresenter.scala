import droid.{ControlChanges, Patches}
import rxscalajs.Observable
import ui.Editor
import midi.webmidi.{Channel, Output, WebMidi}

class EditorPresenter(view: Editor) {
  private val midiPortsChangedObservable =
    midi.enabledObservable.map(_ => ())
      .merge(midi.connectedObservable.map(_ => ()))
      .merge(midi.disconnectedObservable.map(_ => ()))

  private val midiPortsObservable =
    midiPortsChangedObservable.map(_ => WebMidi.outputs)

  private val patchControlChanges: Observable[Seq[(Int,Int)]] =
    view.selectedPatchIndex
      .combineLatestWith(Patches.patchArrayObservable) { (index, patches) => patches(index) }
      .map(ControlChanges.asControlChanges)
      .map(ControlChanges.asControlValuePairs)

  private def transmitControlChanges(midiPort: Output, channel: Channel, controlChanges: Seq[(Int, Int)]): Unit =
    controlChanges.foreach { case (control, value) => midiPort.sendControlChange(control, value, channel) }

  midiPortsObservable.subscribe(view.setOutput(_))

  Patches.patchListObservable.subscribe(view.setPatches(_))

  view.selectedMidiOutputDevice
    .combineLatest(view.selectedMidiOutputChannel, patchControlChanges)
    .subscribe { tuple => transmitControlChanges(tuple._1, tuple._2, tuple._3) }
}
