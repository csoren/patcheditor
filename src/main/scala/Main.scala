import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.Node
import org.scalajs.{dom => jsdom}
import org.scalajs.jquery.jQuery

import scala.scalajs.js

import materialize._

object Main extends js.JSApp {
  @dom
  def patchSelector: Binding[Node] =
    <div class="input-field col s12">
      <select>
        <option value="1">Option 1</option>
        <option value="2">Option 2</option>
        <option value="3">Option 3</option>
      </select>
      <label>Materialize Select</label>
    </div>

  def main(): Unit = {
    dom.render(jsdom.document.getElementById("playground"), patchSelector)
    jQuery("select").material_select()
  }
}
