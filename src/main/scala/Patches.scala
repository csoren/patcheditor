import droid.Patch
import rxscalajs.{Observable, Subject}

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}


object Patches {
  case class OptionValue(index: Int, tag: String, patch: Patch) extends Ordered[OptionValue] {
    override def compare(that: OptionValue): Int =
      this.tag.compare(that.tag) match {
        case 0 => patch.name.compare(that.patch.name)
        case t => t
      }
  }

  private val _patchArraySubject: Subject[js.Array[Patch]] = Subject()

  val patchArrayObservable: Observable[js.Array[Patch]] = _patchArraySubject

  val patchListObservable: Observable[List[OptionValue]] =
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
