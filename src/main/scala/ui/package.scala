import com.thoughtworks.binding.Binding.Var
import materialize._
import org.scalajs.dom.html.{Element, Select, Option => HtmlOption}
import org.scalajs.jquery.{JQueryEventObject, jQuery}
import org.scalajs.{dom => jsdom}
import rxscalajs.Observable
import rxscalajs.subjects.ReplaySubject
import reactive._

package object ui {
  def mkSelect: Select = jsdom.document.createElement("select").asInstanceOf[Select]

  def mkOption(text: String, value: String): HtmlOption = {
    val option = jsdom.document.createElement("option").asInstanceOf[HtmlOption]
    option.text = text
    option.value = value
    option
  }

  implicit final class ElementExt(val element: Element) {
    def eventAsObservable[T](name: String, value: => T): Observable[T] = {
      val observable = ReplaySubject.withSize[T](1)
      jQuery(element).on(name, (_: JQueryEventObject) => observable.next(value))
      observable.next(value)
      observable
    }

    def eventAsVar[T](name: String, value: => T): Var[T] = {
      val variable = Var(value)
      jQuery(element).on(name, (_: JQueryEventObject) => variable := value)
      variable
    }
  }

  implicit final class UiSelectExt(val select: Select) {
    def optionalSelectedIndex: Option[Int] =
      select.selectedIndex match {
        case -1 => None
        case v => Some(v)
      }

    def optionalSelectedElement: Option[HtmlOption] =
      optionalSelectedIndex.map(select.options)

    def selectedIndexObservable: Observable[Option[Int]] =
      select.eventAsObservable("change", optionalSelectedIndex)

    def selectedElementObservable: Observable[Option[HtmlOption]] =
      select.eventAsObservable("change", optionalSelectedElement)

    def selectedIndexVar: Var[Option[Int]] =
      select.eventAsVar("change", optionalSelectedIndex)

    def selectedValueObservable: Observable[Option[String]] =
      selectedIndexObservable.map(_.map(select.options)).map(_.map(_.value))
  }
}
