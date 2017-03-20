import midi.webmidi.{Channel, Output}

class MainPresenter(editorView: ui.Editor, midiView: ui.Midi) {

  private val editorPresenter = new EditorPresenter(editorView)
  private val midiPresenter = new MidiPresenter(midiView)

  private val transmitPatch =
    midiPresenter.selectedMidiOutput
      .combineLatestWith(editorPresenter.patchControlChangesObservable) { (first: (Output,Channel), second: Seq[(Int, Int)]) =>
        (first._1, first._2, second)
      }

  private def transmitControlChanges(midiPort: Output, channel: Channel, controlChanges: Seq[(Int, Int)]): Unit =
    controlChanges.foreach { case (control, value) => midiPort.sendControlChange(control, value, channel) }

  transmitPatch.subscribe { (transmitControlChanges _).tupled }

}
