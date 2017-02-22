package droid

import droid.Patch.MatrixControlledValue
import droid.Waveform.Waveform
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.annotation.JSName
import scala.scalajs.js.|


object MatrixController extends Enumeration {
  type MatrixController = Value

  val En1 = Value(0)
  val En2 = Value(1)
  val DC1 = Value(2)
  val DC2 = Value(3)
  val PB = Value(4)
  val PB2 = Value(5)
  val Mod = Value(6)
  val Vel = Value(7)
  val VlG = Value(8)
  val Gat = Value(9)
  val Aft = Value(10)
  val KF = Value(11)
  val KF2 = Value(12)
  val Hld = Value(13)
  val Exp = Value(14)
  val Bth = Value(15)
}


object Waveform extends Enumeration {
  type Waveform = Value

  val SawUp = Value(0)
  val SawDown = Value(1)
  val Square = Value(2)
  val Triangle = Value(3)
  val Noise = Value(4)
  val `Sample&Hold` = Value(5)
  val Digital = Value(6)
  val Silence = Value(7)
}


object Distortion extends Enumeration {
  type Distortion = Value

  val Clip = Value(0)
  val Mirror = Value(1)
  val ZeroSnap = Value(2)
  val Wrap = Value(3)
}


object TuningMode extends Enumeration {
  type TuningMode = Value

  val Fine = Value(0)
  val Linear = Value(1)
  val Standard = Value(2)
  val Wide = Value(3)
}


@js.native
trait WaveformDistortion extends js.Object {
  @JSName("waveform")
  private[droid] val _waveform: String = js.native

  @JSName("distortion")
  private[droid] val _distortion: String = js.native
}


@js.native
trait DCO extends js.Object {
  val octave: Int = js.native
  val amplitude: MatrixControlledValue = js.native
  val frequency: MatrixControlledValue = js.native
  val offset: MatrixControlledValue = js.native
  val pulseWidth: MatrixControlledValue = js.native
  val waveform: MatrixControlledValue = js.native

  @JSName("tuningMode")
  private[droid] val _tuningMode: String = js.native
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
  type MatrixControlledValue = Any
  type MatrixControlledWaveform = Any

  implicit final class MatrixControlledValueExt(val controlledValue: MatrixControlledValue) {
    def matrixController: Option[MatrixController.MatrixController] =
      controlledValue match {
        case s: String => Some(MatrixController.withName(s))
        case _ => None
      }

    def value: Option[Int] =
      controlledValue match {
        case n: Int => Some(n)
        case _ => None
      }
  }

  implicit final class MatrixControlledWaveformExt(val controlledValue: MatrixControlledWaveform) {
    def matrixController: Option[MatrixController.MatrixController] =
      controlledValue match {
        case s: String => Some(MatrixController.withName(s))
        case _ => None
      }

    def value: Option[WaveformDistortion] =
      controlledValue match {
        case _: String => None
        case waveform => Some(waveform.asInstanceOf[WaveformDistortion])
      }
  }

  implicit final class WaveformDistortionExt(val value: WaveformDistortion) {
    def waveform: Waveform.Waveform = Waveform.withName(value._waveform)
    def distortion: Distortion.Distortion = Distortion.withName(value._distortion)
  }

  implicit final class DCOExt(val value: DCO) {
    def tuningMode: TuningMode.TuningMode = TuningMode.withName(value._tuningMode)
  }

  def load(url: String): Future[js.Array[Patch]] =
    Ajax
      .get(url)
      .map(xhr => js.JSON.parse(xhr.responseText).asInstanceOf[js.Array[Patch]])

}
