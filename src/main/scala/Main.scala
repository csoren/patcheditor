import com.thoughtworks.binding.{Binding, dom}
import droid.Patch
import materialize._
import org.scalajs.dom.Node
import org.scalajs.dom.html.{Select, Option => HtmlOption}
import org.scalajs.jquery.jQuery
import org.scalajs.{dom => jsdom}
import rxscalajs.Subject

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}

object Main extends js.JSApp {
  private case class OptionValue(index: Int, patch: Patch)

  private val patchListObservable: Subject[List[(String, OptionValue)]] = Subject()

  private val allPatches: Future[js.Array[Patch]] =
    droid.Patch
      .load("droidpatches.json")
      .map(_.sortBy(_.name))

  private def categorizedPatches: Future[Map[String, List[OptionValue]]] =
    allPatches
      .map {
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
        patchListObservable.next(patchNames)
      case Failure(str) =>
        println(str)
    }
  }

  private def patchOption(text: String, value: OptionValue): HtmlOption = {
    val option = jsdom.document.createElement("option").asInstanceOf[HtmlOption]
    option.text = text
    option.value = value.index.toString
    option
  }

  @dom
  private def patchSelector: Binding[Select] = {
    val select: Select = <select />
    patchListObservable.subscribe { (patches: List[(String, OptionValue)]) =>
      val options = patches.map { case (text, value) => patchOption(text, value) }
      jQuery(select)
        .empty()
        .append(options:_*)
        .material_select()
    }
    select
  }

  @dom
  private def patchSelectorDiv: Binding[Node] = {
    <div class="input-field col s12">
      { patchSelector.bind }
      <label>Patch</label>
    </div>
  }

  def main(): Unit = {
    dom.render(jsdom.document.getElementById("playground"), patchSelectorDiv)
    jQuery("select").material_select()
    updatePatches()
  }
}
