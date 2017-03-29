package ui.facade

import rxscalajs.Observable
import ui.{facade, polymer}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal


@js.native
@JSGlobal("MyMidi")
class MyEditor extends polymer.Element {
  var selectedPatch: Int = js.native
  var patchItems: js.Array[String] = js.native
}


object MyEditor {
  implicit class MyEditorExt(val editor: MyEditor) extends AnyVal {
    def patchObservable: Observable[Int] =
      editor.observableOf("selected-patch-changed")(editor.selectedPatch)
  }
}