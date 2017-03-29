import scala.scalajs.js
import org.scalajs.dom.document

object Main extends js.JSApp {
  val app = new ui.App(document.getElementById("app").asInstanceOf[ui.MyApp])
  val presenter = new MainPresenter(app.editor, app.midi)

  override def main(): Unit = {
    midi.enable()
  }
}
