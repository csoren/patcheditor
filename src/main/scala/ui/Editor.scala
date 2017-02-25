package ui

import com.thoughtworks.binding.{Binding, dom}
import droid.Patches
import org.scalajs.dom.Node
import org.scalajs.dom.html.{Select, Option => HtmlOption}
import org.scalajs.{dom => jsdom}
import rxscalajs.Observable
import midi.webmidi.{Channel, Output, Single}

class Editor() {

  private val patchSelector = mkSelect()

  @dom
  private def patchSelectorDiv: Binding[Node] = {
    <div class="row">
      <div class="input-field col s12 m6 offset-m3">
        { patchSelector }
        <label>Patch</label>
      </div>
    </div>
  }

  def setPatches(patches: Seq[Patches.OptionValue]): Unit = {
    val options = patches.map { p => mkOption(s"[${p.category}] ${p.patch.name}", p.index.toString) }
    patchSelector.setMaterialOptions(options)
  }

  val selectedPatchIndex: Observable[Int] =
    patchSelector.selectedIndexObservable.filter(_ >= 0)
      .map(n => patchSelector.options(n).value.toInt)

  @dom
  def layout: Binding[Node] =
    <div>
      { patchSelectorDiv.bind }
    </div>
}
