import droid.{ControlChanges, Patches}
import reactive._
import rxscalajs.Observable
import ui.Editor

class EditorPresenter(view: Editor) {
  val patchControlChangesObservable: Observable[Seq[(Int,Int)]] =
    view.selectedPatch
      .flatMap(_.toObservable)
      .map(ControlChanges.asControlChanges)
      .map(ControlChanges.asControlValuePairs)

  Patches.patchListObservable.map(_.toIndexedSeq).subscribe(view.setPatches _)
}
