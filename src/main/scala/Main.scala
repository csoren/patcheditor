import com.thoughtworks.binding.{Binding, dom}
import droid.Patch
import materialize._
import org.scalajs.dom.Node
import org.scalajs.dom.html.{Select, Option => HtmlOption}
import org.scalajs.jquery.jQuery
import org.scalajs.{dom => jsdom}
import rxscalajs.Subject
import rxscalajs.subjects.ReplaySubject
import webmidi.{PortState, WebMidi}

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}

object Main extends js.JSApp {
  private case class OptionValue(index: Int, patch: Patch)

  private val patchListObservable: Subject[List[(String, OptionValue)]] = Subject()

  private val midiEnabledObservable: Subject[Boolean] = ReplaySubject.withSize(1)

  private val midiConnectedObservable: Subject[webmidi.Event] = Subject()

  private val midiDisconnectedObservable: Subject[webmidi.Event] = Subject()

  midiEnabledObservable.filter(_ == true).subscribe { _ =>
    WebMidi.addListener(PortState.connected) { midiConnectedObservable.next }
    WebMidi.addListener(PortState.disconnected) { midiDisconnectedObservable.next }
  }

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
        patchListObservable.next(patchNames)
      case Failure(str) =>
        println(str)
    }
  }

  private def mkOption(text: String, value: String): HtmlOption = {
    val option = jsdom.document.createElement("option").asInstanceOf[HtmlOption]
    option.text = text
    option.value = value
    option
  }

  private def patchOption(text: String, value: OptionValue): HtmlOption =
    mkOption(text, value.index.toString)

  private def updateOptions(select: Select, options: Seq[HtmlOption]): Unit =
    jQuery(select)
      .empty()
      .append(options:_*)
      .material_select()

  @dom
  private def patchSelector: Binding[Select] = {
    val select: Select = <select />
    patchListObservable.subscribe { (patches: List[(String, OptionValue)]) =>
      val options = patches.map { case (text, value) => patchOption(text, value) }
      updateOptions(select, options)
    }
    select
  }

  @dom
  private def midiOutputDevice: Binding[Select] = {
    val select = jsdom.document.createElement("select").asInstanceOf[Select]
    val events =
      midiEnabledObservable.map(_ => ())
        .merge(midiConnectedObservable.map(_ => ()))
        .merge(midiDisconnectedObservable.map(_ => ()))

    events.subscribe { _ =>
      val options = WebMidi.outputs.map { p => mkOption(p.name, p.id) }
      updateOptions(select, options)
    }
    select
  }

  @dom
  private def midiOutputChannel: Binding[Select] = {
    val select = jsdom.document.createElement("select").asInstanceOf[Select]
    val options = (1 to 16).map(_.toString).map(v => mkOption(v, v))
    options.foreach(select.appendChild)
    select
  }

  @dom
  private def patchSelectorDiv: Binding[Node] = {
    <div class="row">
      <div class="input-field col s12 m6 offset-m3">
        { patchSelector.bind }
        <label>Patch</label>
      </div>
    </div>
  }

  @dom
  private def midiOutputDiv: Binding[Node] = {
    <div class="row">
      <div class="input-field col offset-m3 s10 m5">
        { midiOutputDevice.bind }
        <label>MIDI output device</label>
      </div>
      <div class="input-field col s2 m1">
        { midiOutputChannel.bind }
        <label>Channel</label>
      </div>
    </div>
  }

  @dom
  private def layout: Binding[Node] =
    <div>
      { midiOutputDiv.bind }
      { patchSelectorDiv.bind }
    </div>

  private def enableMidi(): Unit =
    WebMidi.enable(sysex = false) { error =>
      if (error.isEmpty)
        midiEnabledObservable.next(true)
      else
        println(error.get)
    }

  def main(): Unit = {
    dom.render(jsdom.document.getElementById("playground"), layout)
    jQuery("select").material_select()
    updatePatches()
    enableMidi()
  }
}
