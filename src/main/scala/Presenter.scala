import droid.ControlChanges
import rxscalajs.Observable
import webmidi.{Channel, Output, WebMidi}

class Presenter(view: View) {
  private val midiPortsChangedObservable =
    Midi.enabledObservable.map(_ => ())
      .merge(Midi.connectedObservable.map(_ => ()))
      .merge(Midi.disconnectedObservable.map(_ => ()))

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
