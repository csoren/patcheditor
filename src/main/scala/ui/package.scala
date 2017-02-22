import com.thoughtworks.binding.Binding.Var
import org.scalajs.dom.html.{Element, Select}
import org.scalajs.jquery.{JQueryEventObject, jQuery}
import rxscalajs.subjects.ReplaySubject
import rxscalajs.{Observable, Subject}

package object ui {
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

  implicit final class SelectExt(val select: Select) {
    def selectedIndexObservable: Observable[Int] =
      select.eventAsObservable("change", select.selectedIndex)

    def selectedIndexVar: Var[Int] =
      select.eventAsVar("change", select.selectedIndex)
  }
}
