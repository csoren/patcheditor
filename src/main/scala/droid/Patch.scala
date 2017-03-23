package droid

import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import scala.language.implicitConversions
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.annotation.JSName


object MatrixControllerType extends Enumeration {
  type MatrixControllerType = Value

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
trait MatrixControllableWaveform extends js.Any

@js.native
trait MatrixControllableValue extends js.Any


@js.native
trait WaveformDistortion extends js.Object


@js.native
private[droid] trait WaveformDistortionFacade extends WaveformDistortion {
  @JSName("waveform")
  val _waveform: String = js.native

  @JSName("distortion")
  val _distortion: String = js.native
}


@js.native
trait DCO extends js.Object {
  val octave: Int = js.native
  val amplitude: MatrixControllableValue = js.native
  val frequency: MatrixControllableValue = js.native
  val offset: MatrixControllableValue = js.native
  val pulseWidth: MatrixControllableValue = js.native
  val waveform: MatrixControllableWaveform = js.native
}

@js.native
private[droid] trait DCOFacade extends DCO {
  @JSName("tuningMode")
  val _tuningMode: String = js.native
}


@js.native
trait Envelope extends js.Object {
  val attack: Int = js.native
  val decay: Int = js.native
  val attackLevel: Int = js.native
  val release: Int = js.native
  val sustain: Int = js.native
  val offset: MatrixControllableValue = js.native
}

@js.native
trait Patch extends js.Object {
  val name: String = js.native

  val author: String = js.native
  val comment: String = js.native
  val dco1: DCO = js.native
  val dco2: DCO = js.native
  val env1: Envelope = js.native
  val env2: Envelope = js.native
  val dco2Env2Step: Int = js.native
  val arpeggio: MatrixControllableValue = js.native
  val filterFrequency1: MatrixControllableValue = js.native
  val filterWidthFrequency2: MatrixControllableValue = js.native
  val variousModes: Int = js.native
  val mixingStructure: Int = js.native
}

@js.native
private[droid] trait PatchFacade extends Patch {
  @JSName("tags")
  val _tags: js.Array[String] = js.native
}


object Patch {
  implicit final class PatchExt(val patch: Patch) extends AnyVal {
    private implicit def toFacade(v: Patch): PatchFacade = v.asInstanceOf[PatchFacade]

    def tags: js.Array[String] =
      if (patch._tags.length == 0)
        js.Array("Generic")
      else
        patch._tags
  }

  object MatrixController {
    def unapply(value: MatrixControllableValue): Option[MatrixControllerType.MatrixControllerType] =
      value.asInstanceOf[Any] match {
        case s: String => Some(MatrixControllerType.withName(s))
        case _ => None
      }

    def unapply(value: MatrixControllableWaveform): Option[MatrixControllerType.MatrixControllerType] =
      value.asInstanceOf[Any] match {
        case s: String => Some(MatrixControllerType.withName(s))
        case _ => None
      }
  }

  object ConstantValue {
    def unapply(value: MatrixControllableValue): Option[Int] =
      value.asInstanceOf[Any] match {
        case n: Int => Some(n)
        case _ => None
      }
  }

  object Waveform {
    def unapply(value: MatrixControllableWaveform): Option[WaveformDistortion] =
      value.asInstanceOf[Any] match {
        case waveform: js.Object => Some(waveform.asInstanceOf[WaveformDistortion])
        case _ => None
    }
  }

  implicit final class WaveformDistortionExt(val value: WaveformDistortion) {
    private implicit def toFacade(v: WaveformDistortion): WaveformDistortionFacade = v.asInstanceOf[WaveformDistortionFacade]

    def waveform: droid.Waveform.Waveform = droid.Waveform.withName(value._waveform)
    def distortion: Distortion.Distortion = Distortion.withName(value._distortion)
  }

  implicit final class DCOExt(val value: DCO) {
    private implicit def toFacade(v: DCO): DCOFacade = v.asInstanceOf[DCOFacade]

    def tuningMode: TuningMode.TuningMode = TuningMode.withName(value._tuningMode)
  }

  def load(url: String): Future[js.Array[Patch]] =
    Ajax
      .get(url)
      .map(xhr => js.JSON.parse(xhr.responseText).asInstanceOf[js.Array[Patch]])

}
