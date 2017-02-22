import droid.Patch
import rxscalajs.{Observable, Subject}

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}


object Patches {
  case class OptionValue(index: Int, patch: Patch)

  private val _patchListSubject: Subject[List[(String, OptionValue)]] = Subject()

  def patchListObservable: Observable[List[(String, OptionValue)]] = _patchListSubject

  private val allPatches: Future[js.Array[Patch]] =
    droid.Patch
      .load("droidpatches.json")
      .map(_.sortBy(_.name))

  private def categorizedPatches: Future[Map[String, List[OptionValue]]] =
    allPatches.map {
      _.toList
        .zipWithIndex
        .map { case (patch, index) => OptionValue(index, patch) }
        .flatMap(v => v.patch.tags.map((_, v)))
        .groupBy(_._1)
        .map { case (tag, list) => (tag, list.map(_._2)) }
        .toMap
    }

  private def updatePatches(): Unit = {
    categorizedPatches.onComplete {
      case Success(patches) =>
        val patchNames =
          patches.toList
            .sortBy(_._1)
            .flatMap { case (tag, patchList) => patchList.map(p => (s"[$tag] ${p.patch.name}", p)) }
        _patchListSubject.next(patchNames)
      case Failure(str) =>
        println(str)
    }
  }

  updatePatches()
}
