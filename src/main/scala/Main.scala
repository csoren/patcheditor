import com.thoughtworks.binding.Binding.Vars
import com.thoughtworks.binding.{Binding, dom}
import droid.Patch
import materialize._
import org.scalajs.dom.Node
import org.scalajs.dom.html.Select
import org.scalajs.dom.html.{Option => HtmlOption}
import org.scalajs.jquery.jQuery
import org.scalajs.{dom => jsdom}
import rxscalajs.Subject

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.Array
import scala.util.{Failure, Success}

object Main extends js.JSApp {
  private val patchListObservable: Subject[List[(String, Patch)]] = Subject()

  private val allPatches: Future[Array[Patch]] =
    droid.Patch.load("droidpatches.json").map(_.sortBy(_.name))

  private def categorizedPatches: Future[Map[String, List[Patch]]] =
    allPatches.map {
      _.toList
        .flatMap(patch => patch.tags.map((_, patch)))
        .groupBy(_._1)
        .map { case (tag, list) => (tag, list.map(_._2)) }
        .toMap
    }

  private def updatePatches(): Unit = {
    categorizedPatches.onComplete {
      case Success(patches) =>
        val patchNames = patches.toList.flatMap { case (tag, patchList) => patchList.map(p => (s"[$tag] ${p.name}", p)) }
        patchListObservable.next(patchNames)
      case Failure(str) =>
        println(str)
    }
  }

  private def patchOption(text: String, value: Patch): HtmlOption = {
    val option = jsdom.document.createElement("option").asInstanceOf[HtmlOption]
    option.text = text
    option
  }

  @dom
  private def patchSelector: Binding[Select] = {
    val select: Select = <select />
    patchListObservable.subscribe { (patches: List[(String, Patch)]) =>
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
