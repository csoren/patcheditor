import org.scalajs.dom.html.{Select, Option => HtmlOption}
import org.scalajs.jquery.{JQuery, jQuery}

import scala.language.implicitConversions

package object materialize {
  implicit def jquery2Materialize(jquery: JQuery): MaterializeJQuery =
    jquery.asInstanceOf[MaterializeJQuery]

  implicit final class MaterializeSelectExt(val select: Select) {
    def setMaterialOptions(options: Seq[HtmlOption]): Unit =
      jQuery(select)
        .empty()
        .append(options: _*)
        .change()
        .material_select()
  }
}
