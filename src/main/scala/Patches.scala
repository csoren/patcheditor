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

  private val _patchListSubject: Subject[List[OptionValue]] = Subject()

  def patchListObservable: Observable[List[OptionValue]] = _patchListSubject

  private val allPatches: Future[js.Array[Patch]] =
    droid.Patch
      .load("droidpatches.json")
      .map(_.sortBy(_.name))

  private def categorizedPatches: Future[List[OptionValue]] =
    allPatches.map {
      _.toList
        .zipWithIndex
        .flatMap { case (patch, index) => patch.tags.map(OptionValue(index, _, patch)) }
        .sorted
    }

  private def updatePatches(): Unit = {
    categorizedPatches.onComplete {
      case Success(patches) =>
        _patchListSubject.next(patches)
      case Failure(str) =>
        println(str)
    }
  }

  updatePatches()
}
