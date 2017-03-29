package ui.polymer

import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import rxscalajs.Observable

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal("Polymer.Element")
class Element extends HTMLElement {

}


object Element {
  implicit class ElementExt(val element: Element) extends AnyVal {
    def observableOf[T](name: String)(value: => T): Observable[T] = {
      Observable.create[T] { observer =>
        val fn: js.Function1[dom.raw.Event,_] = (_: dom.raw.Event) => observer.next(value)
        element.addEventListener(name, fn)
        () => element.removeEventListener(name, fn)
      }
    }
  }
}