import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.{Binding, dom}
import droid.{ControlChange, ControlChangeMode, ControlChanges}
import materialize._
import org.scalajs.dom.Node
import org.scalajs.dom.html.{Select, Option => HtmlOption}
import org.scalajs.jquery.jQuery
import org.scalajs.{dom => jsdom}
import rxscalajs.{Observable, Subject}
import rxscalajs.subjects.ReplaySubject
import reactive._
import ui._
import webmidi._

import scala.scalajs.js


object Main extends js.JSApp {

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
      .change()
      .material_select()

  private val patchSelector: Select = {
    val select = mkSelect()
    Patches.patchListObservable.subscribe { (patches: List[Patches.OptionValue]) =>
      val options = patches.map { p => mkOption(s"[${p.category}] ${p.patch.name}", p.index.toString) }
      updateOptions(select, options)
    }
    select
  }

  private val midiPortsChangedObservable =
    Midi.enabledObservable.map(_ => ())
      .merge(Midi.connectedObservable.map(_ => ()))
      .merge(Midi.disconnectedObservable.map(_ => ()))

  private val midiOutputDevice: Select = {
    val select = mkSelect()

    midiPortsChangedObservable.subscribe { _ =>
      val options = WebMidi.outputs.map { p => mkOption(p.name, p.id) }
      updateOptions(select, options)
    }
    select
  }

  private val selectedMidiOutputDevice: Observable[Output] =
    midiOutputDevice.selectedIndexObservable.map(n => WebMidi.outputs(n))

  private val midiOutputChannel: Select = {
    val select = mkSelect()
    val options = (1 to 16).map(_.toString).map(v => mkOption(v, v))
    options.foreach(select.appendChild)
    select
  }

  private val selectedMidiOutputChannel: Observable[Channel] =
    midiOutputChannel.selectedIndexObservable.map(n => Single(n+1))

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
        { midiOutputDevice }
        <label>MIDI output device</label>
      </div>
      <div class="input-field col s2 m1">
        { midiOutputChannel }
        <label>Channel</label>
      </div>
    </div>
  }

  private def asControllerValuePair(mode: ControlChangeMode.ControlChangeMode): (Int, Int) =
    (16, mode.id)

  private def asControllerValuePairs(controlChanges: (ControlChangeMode.ControlChangeMode, Seq[ControlChange])): Seq[(Int, Int)] =
    asControllerValuePair(controlChanges._1) +: controlChanges._2.map(v => (v.control, v.value))

  private def asControllerValuePairs(controlChanges: Seq[ControlChange]): Seq[(Int, Int)] =
    controlChanges
      .groupBy(_.mode)
      .toSeq
      .flatMap(asControllerValuePairs) :+
      asControllerValuePair(ControlChangeMode.double)

  private val patchControlChanges: Observable[Seq[(Int,Int)]] =
    patchSelector.selectedIndexObservable.filter(_ >= 0)
      .map(n => patchSelector.options(n).value.toInt)
      .combineLatestWith(Patches.patchArrayObservable) { (index, patches) => patches(index) }
      .map(ControlChanges.asControlChanges)
      .map(asControllerValuePairs)

  @dom
  private def layout: Binding[Node] =
    <div>
      { midiOutputDiv.bind }
      { patchSelectorDiv.bind }
    </div>

  private def transmitControlChanges(midiPort: Output, channel: Channel, controlChanges: Seq[(Int, Int)]): Unit =
    controlChanges.foreach { case (control, value) => midiPort.sendControlChange(control, value, channel) }

  def main(): Unit = {
    dom.render(jsdom.document.getElementById("playground"), layout)
    jQuery("select").material_select()
    Midi.enable()

    selectedMidiOutputDevice.combineLatest(selectedMidiOutputChannel, patchControlChanges)
      .debugLog("combined")
      .subscribe { tuple => transmitControlChanges(tuple._1, tuple._2, tuple._3) }
  }
}
