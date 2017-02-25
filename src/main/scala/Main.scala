import com.thoughtworks.binding.dom
import materialize._
import org.scalajs.jquery.jQuery
import org.scalajs.{dom => jsdom}

import scala.scalajs.js


object Main extends js.JSApp {

  def main(): Unit = {
    val editorView = new ui.Editor()
    dom.render(jsdom.document.getElementById("editor"), editorView.layout)

    val midiView = new ui.Midi()
    dom.render(jsdom.document.getElementById("midi"), midiView.layout)

    val presenter = new MainPresenter(editorView, midiView)

    jQuery("select").material_select()
    jQuery(".button-collapse").sideNav()

    midi.enable()
  }
}
