import midi.webmidi._
import reactive._

class MainPresenter(editorView: ui.Editor, midiView: ui.Midi) {

  private val editorPresenter = new EditorPresenter(editorView)
  private val midiPresenter = new MidiPresenter(midiView)

  private val transmitPatch =
    midiPresenter.selectedMidiOutput
      .combineLatestWith(editorPresenter.patchControlChangesObservable) { (first: (Output,Channel), second: Seq[(Int, Int)]) =>
        (first._1, first._2, second)
      }

  private val noteOn =
    midiPresenter.selectedMidiInput
      .map(v => v._1.observableOf(InputEventName.noteon, v._2))
      .switch

  private val noteOff =
    midiPresenter.selectedMidiInput
      .map(v => v._1.observableOf(InputEventName.noteoff, v._2))
      .switch

  private def transmitControlChanges(midiPort: Output, channel: Channel, controlChanges: Seq[(Int, Int)]): Unit =
    controlChanges.foreach { case (control, value) => midiPort.sendControlChange(control, value, channel) }

  transmitPatch.subscribe { (transmitControlChanges _).tupled }

  private def transmitNoteOn(noteOnEvent: NoteOnEvent, midiPort: Output, channel: Channel): Unit = {
    midiPort.playNote(noteOnEvent.note.number, channel, noteOnEvent.rawVelocity)
  }

  noteOn.withLatestFrom(midiPresenter.selectedMidiOutput)
    .map(v => (v._1, v._2._1, v._2._2))
    .subscribe { (transmitNoteOn _).tupled }

  private def transmitNoteOff(noteOffEvent: NoteOffEvent, midiPort: Output, channel: Channel): Unit = {
    midiPort.stopNote(noteOffEvent.note.number, channel, noteOffEvent.rawVelocity)
  }

  noteOff.withLatestFrom(midiPresenter.selectedMidiOutput)
    .map(v => (v._1, v._2._1, v._2._2))
    .subscribe { (transmitNoteOff _).tupled }

}
