import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.{Binding, dom}
import droid.{ControlChange, ControlChanges}
import materialize._
import org.scalajs.dom.Node
import org.scalajs.dom.html.{Select, Option => HtmlOption}
import org.scalajs.jquery.jQuery
import org.scalajs.{dom => jsdom}
import rxscalajs.{Observable, Subject}
import rxscalajs.subjects.ReplaySubject
import reactive._
import ui._
import webmidi.{PortState, WebMidi}

import scala.scalajs.js


object Main extends js.JSApp {

  private val midiEnabledObservable: Subject[Boolean] = ReplaySubject.withSize(1)

  private val midiConnectedObservable: Subject[webmidi.Event] = Subject()

  private val midiDisconnectedObservable: Subject[webmidi.Event] = Subject()

  midiEnabledObservable.filter(_ == true).subscribe { _ =>
    WebMidi.addListener(PortState.connected) { midiConnectedObservable.next }
    WebMidi.addListener(PortState.disconnected) { midiDisconnectedObservable.next }
  }

  private def mkSelect(): Select = jsdom.document.createElement("select").asInstanceOf[Select]

  private def mkOption(text: String, value: String): HtmlOption = {
    val option = jsdom.document.createElement("option").asInstanceOf[HtmlOption]
    option.text = text
    option.value = value
    option
  }

  private def updateOptions(select: Select, options: Seq[HtmlOption]): Unit =
    jQuery(select)
      .empty()
      .append(options:_*)
      .material_select()

  private val patchSelector: Select = {
    val select = mkSelect()
    Patches.patchListObservable.subscribe { (patches: List[Patches.OptionValue]) =>
      val options = patches.map { p => mkOption(s"[${p.category}] ${p.patch.name}", p.index.toString) }
      updateOptions(select, options)
      select.selectedIndex = 0
    }
    select
  }

  @dom
  private def midiOutputDevice: Binding[Select] = {
    val select = mkSelect()
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
    val select = <select/>
    val options = (1 to 16).map(_.toString).map(v => mkOption(v, v))
    options.foreach(select.appendChild)
    select
  }

  @dom
  private def patchSelectorDiv: Binding[Node] = {
    <div class="row">
      <div class="input-field col s12 m6 offset-m3">
        { patchSelector }
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

  private val patchControlChanges: Var[Seq[ControlChange]] =
    patchSelector.selectedIndexObservable.filter(_ >= 0)
      .combineLatestWith(Patches.patchArrayObservable) { (index, patches) => patches(index) }
      .map(ControlChanges.asControlChanges)
      .toVar(Seq.empty)

  @dom
  private def layout: Binding[Node] =
    <div>
      { midiOutputDiv.bind }
      { patchSelectorDiv.bind }
      { patchControlChanges.bind.mkString(",") }
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
    enableMidi()
  }
}
