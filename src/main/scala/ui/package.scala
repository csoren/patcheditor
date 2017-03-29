import org.scalajs.dom.html.{Element, Select, Option => HtmlOption}
import org.scalajs.{dom => jsdom}
import rxscalajs.Observable
import rxscalajs.subjects.ReplaySubject
import reactive._
import ui.polymer

package object ui {
/*
  implicit final class ElementExt(val element: polymer.Element) {
    def eventAsObservable[T](name: String, value: => T): Observable[T] = {
      val observable = ReplaySubject.withSize[T](1)
      jQuery(element).on(name, (_: JQueryEventObject) => observable.next(value))
      observable.next(value)
      observable
    }
  }
  */
}
