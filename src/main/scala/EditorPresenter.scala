import droid.{ControlChanges, Patches}
import rxscalajs.Observable
import ui.Editor
import reactive._
import midi.webmidi.{Channel, Output, WebMidi}

class EditorPresenter(view: Editor) {
  val patchControlChangesObservable: Observable[Seq[(Int,Int)]] =
    view.selectedPatchIndex
      .combineLatestWith(Patches.patchArrayObservable) { (index, patches) => index.map(patches) }
      .flatMap(_.toObservable)
      .map(ControlChanges.asControlChanges)
      .map(ControlChanges.asControlValuePairs)

  Patches.patchListObservable.subscribe(view.setPatches(_))
}
