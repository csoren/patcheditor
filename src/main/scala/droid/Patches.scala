package droid

import rxscalajs.subjects.ReplaySubject
import rxscalajs.{Observable, Subject}

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}


object Patches {
  case class OptionValue(index: Int, category: String, patch: Patch) extends Ordered[OptionValue] {
    override def compare(that: OptionValue): Int =
      this.category.compare(that.category) match {
        case 0 => patch.name.compare(that.patch.name)
        case t => t
      }
  }

  private val _patchArraySubject: Subject[js.Array[Patch]] = ReplaySubject.withSize(1)

  def patchArrayObservable: Observable[js.Array[Patch]] = _patchArraySubject

  def patchListObservable: Observable[List[OptionValue]] =
    _patchArraySubject.map {
      _.toList
      .zipWithIndex
      .flatMap { case (patch, index) => patch.tags.map(OptionValue(index, _, patch)) }
      .sorted
    }

  private def loadPatches: Future[js.Array[Patch]] =
    droid.Patch.load("droidpatches.json")

  loadPatches.onComplete {
    case Success(patches) =>
      _patchArraySubject.next(patches)
    case Failure(str) =>
      println(str)
  }
}
