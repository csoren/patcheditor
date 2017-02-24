import com.thoughtworks.binding.Binding.Var
import rxscalajs.{Observable, Observer}

import scala.scalajs.js
import org.scalajs.{dom => jsdom}

package object reactive {
  implicit final class ObservableExt[T](val observable: Observable[T]) {
    def toVar(initialValue: T): Var[T] = {
      val v = Var[T](initialValue)
      observable.subscribe((t: T) => v := t)
      v
    }

    def forEach(fn: T => _): Observable[T] =
      observable.map(v => { fn(v); v })

    def debugLog(name: String): Observable[T] =
      Observable.create { (observer: Observer[T]) =>
        println(s"$name.subscribe")
        val subscription = observable.subscribe(
          (value: T) => { println(s"$name.next($value)"); observer.next(value) },
          (error: js.Any) => {
            print(s"$name.error: ")
            jsdom.console.log(error)
            observer.error(error)
          },
          () => { println(s"$name.complete()"); observer.complete() }
        )

        () => {
          println(s"$name.unsubscribe")
          subscription.unsubscribe()
        }
      }
  }
}
