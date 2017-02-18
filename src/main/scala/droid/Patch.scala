package droid

import droid.Patch.MatrixControlledValue
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.|

@js.native
trait MatrixController extends js.Object {
  val waveform: String = js.native
  val distortion: String = js.native
}

@js.native
trait DCO extends js.Object {
  val octave: Int = js.native
  val amplitude: MatrixControlledValue = js.native
  val frequency: MatrixControlledValue = js.native
  val offset: MatrixControlledValue = js.native
  val pulseWidth: MatrixControlledValue = js.native
  val waveform: MatrixControlledValue = js.native
  val tuningMode: String = js.native
}

@js.native
trait Envelope extends js.Object {
  val attack: Int = js.native
  val decay: Int = js.native
  val attackLevel: Int = js.native
  val release: Int = js.native
  val sustain: Int = js.native
  val offset: MatrixControlledValue = js.native
}

@js.native
trait Patch extends js.Object {
  val name: String = js.native
  val tags: js.Array[String] = js.native
  val author: String = js.native
  val comment: String = js.native
  val dco1: DCO = js.native
  val dco2: DCO = js.native
  val env1: Envelope = js.native
  val env2: Envelope = js.native
  val dco2Env2Step: Int = js.native
  val arpeggio: MatrixControlledValue = js.native
  val filterFrequency1: MatrixControlledValue = js.native
  val filterWidthFrequency2: MatrixControlledValue = js.native
  val variousModes: Int = js.native
  val mixingStructure: Int = js.native
}

object Patch {
  type MatrixControlledValue = String | MatrixController

  def load(url: String): Future[js.Array[Patch]] =
    Ajax
      .get(url)
      .map(xhr => js.JSON.parse(xhr.responseText).asInstanceOf[js.Array[Patch]])

}
