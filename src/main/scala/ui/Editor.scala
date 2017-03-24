package ui

import com.thoughtworks.binding.{Binding, dom}
import droid.Patches
import materialize._
import org.scalajs.dom.Node
import rxscalajs.Observable

class Editor() {

  private val patchSelector = mkSelect

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

  val selectedPatchIndex: Observable[Option[Int]] =
    patchSelector.selectedElementObservable
      .map(_.map(_.value.toInt))

  @dom
  def layout: Binding[Node] =
    <div>
      { patchSelectorDiv.bind }
    </div>
}
