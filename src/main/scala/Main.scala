import scala.scalajs.js
import org.scalajs.dom.document
import ui.facade.MyApp

object Main extends js.JSApp {
  val app = new ui.App(document.getElementById("app").asInstanceOf[MyApp])
  val presenter = new MainPresenter(app.editor, app.midi)

  override def main(): Unit = {
    midi.enable()
  }
}
