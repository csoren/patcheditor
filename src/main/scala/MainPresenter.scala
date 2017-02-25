import midi.webmidi.{Channel, Output}

class MainPresenter(editorView: ui.Editor, midiView: ui.Midi) {

  private val editorPresenter = new EditorPresenter(editorView)
  private val midiPresenter = new MidiPresenter(midiView)

  private def transmitControlChanges(midiPort: Output, channel: Channel, controlChanges: Seq[(Int, Int)]): Unit =
    controlChanges.foreach { case (control, value) => midiPort.sendControlChange(control, value, channel) }

  midiPresenter.selectedMidiOutput
    .combineLatest(editorPresenter.patchControlChangesObservable)
    .subscribe { tuple => transmitControlChanges(tuple._1._1, tuple._1._2, tuple._2) }

}
