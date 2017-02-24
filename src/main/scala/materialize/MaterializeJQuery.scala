package materialize

import org.scalajs.jquery.JQuery

import scala.scalajs.js

@js.native
trait MaterializeJQuery extends JQuery {
  def material_select(): Unit = js.native
}
