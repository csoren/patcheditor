import org.scalajs.{dom => jsdom}
import rxscalajs.{Observable, Observer}

import scala.scalajs.js

package object reactive {
  implicit final class OptionExt[T](val option: Option[T]) {
    def toObservable: Observable[T] =
      option match {
        case Some(v) => Observable.just(v)
        case None => Observable.empty
    }
  }

  implicit final class ObservableOptionExt[T](val observable: Observable[Option[T]]) extends AnyVal {
    def flattenOption: Observable[T] =
      observable.flatMap(_.toObservable)
  }

  implicit final class ObservableExt[T](val observable: Observable[T]) extends AnyVal {
    def flatMapOption(fn: T => Option[T]): Observable[T] =
      observable.flatMap(t => fn(t).toObservable)

    def foreach(fn: T => _): Observable[T] =
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
