package ui

import rxscalajs.Observable

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal("MyMidi")
class MyMidi extends polymer.Element {
  var selectedOutputChannel: js.UndefOr[Int] = js.native
  var selectedOutputPort: js.UndefOr[Int] = js.native
  var outputPortItems: js.Array[String] = js.native
  var selectedInputChannel: js.UndefOr[Int] = js.native
  var selectedInputPort: js.UndefOr[Int] = js.native
  var inputPortItems: js.Array[String] = js.native
}


object MyMidi {
  implicit class MyMidiExt(val midi: MyMidi) extends AnyVal {
    def outputChannelObservable: Observable[js.UndefOr[Int]] =
      midi.observableOf("selected-output-channel-changed")(midi.selectedOutputChannel)
        .startWith(midi.selectedOutputChannel)

    def outputPortObservable: Observable[js.UndefOr[Int]] =
      midi.observableOf("selected-output-port-changed")(midi.selectedOutputPort)

    def inputChannelObservable: Observable[js.UndefOr[Int]] =
      midi.observableOf("selected-input-channel-changed")(midi.selectedInputChannel)
        .startWith(midi.selectedInputChannel)

    def inputPortObservable: Observable[js.UndefOr[Int]] =
      midi.observableOf("selected-input-port-changed")(midi.selectedInputPort)
  }
}