import com.thoughtworks.binding.dom
import materialize._
import org.scalajs.jquery.jQuery
import org.scalajs.{dom => jsdom}

import scala.scalajs.js


object Main extends js.JSApp {

  def main(): Unit = {
    val view = new View()
    dom.render(jsdom.document.getElementById("playground"), view.layout)
    jQuery("select").material_select()

    val presenter = new Presenter(view)

    Midi.enable()
  }
}
