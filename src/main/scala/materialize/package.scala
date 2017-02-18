import org.scalajs.jquery.JQuery

import scala.language.implicitConversions

package object materialize {
  implicit def jquery2Materialize(jquery: JQuery): MaterializeJQuery =
    jquery.asInstanceOf[MaterializeJQuery]
}
