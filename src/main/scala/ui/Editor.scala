package ui

import droid.{Patch, Patches}
import rxscalajs.Observable

import scala.scalajs.js

class Editor(ui: facade.MyEditor) {

  private var _patches: IndexedSeq[Patches.OptionValue] = IndexedSeq.empty

  def setPatches(patches: IndexedSeq[Patches.OptionValue]): Unit = {
    _patches = patches
    ui.patchItems = js.Array(patches.map(v => s"[${v.category}] ${v.patch.name}") :_*)
  }

  private def optionalPatchIndex: Observable[Option[Int]] =
    ui.patchObservable.map {
      case -1 => None
      case n => Some(n)
    }

  val selectedPatch: Observable[Option[Patch]] =
    optionalPatchIndex.map(_.map(_patches).map(_.patch))
}
