package droid


object ControlChangeMode extends Enumeration {
  type ControlChangeMode = Value

  val double = Value(0)
  val unchanged = Value(1)
  val add128 = Value(2)
  val matrixController = Value(3)
}


case class ControlChange(mode: ControlChangeMode.ControlChangeMode, control: Int, value: Int)


object ControlChanges {
  import Patch._

  private def asControlChanges(value: Int, cc: Int): ControlChange =
    if (value <= 127)
      ControlChange(ControlChangeMode.unchanged, cc, value)
    else
      ControlChange(ControlChangeMode.add128, cc, value - 128)

  private def asControlChanges(value: Int, tuningMode: TuningMode.TuningMode, cc: Int): ControlChange =
    asControlChanges((tuningMode.id << 4) | value, cc)

  private def asControlChanges(controller: MatrixControllerType.MatrixControllerType, cc: Int): ControlChange =
    ControlChange(ControlChangeMode.matrixController, cc, controller.id)

  private def asControlChanges(controlledValue: MatrixControlledValue, cc: Int): ControlChange =
    controlledValue match {
      case MatrixController(controller) => asControlChanges(controller, cc)
      case ConstantValue(value) => asControlChanges(value, cc)
    }

  private def asControlChanges(value: DCO, baseCC: Int): Seq[ControlChange] =
    Seq(
      asControlChanges(value.octave, value.tuningMode, 4 + baseCC),
      asControlChanges(value.amplitude, 3 + baseCC),
      asControlChanges(value.frequency, 5 + baseCC),
      asControlChanges(value.offset, 2 + baseCC),
      asControlChanges(value.pulseWidth, 1 + baseCC),
      asControlChanges(value.waveform, 0 + baseCC)
    )

  private def asControlChanges(value: Envelope, baseCC: Int): Seq[ControlChange] =
    Seq(
      asControlChanges(value.attack, 0 + baseCC),
      asControlChanges(value.decay, 2 + baseCC),
      asControlChanges(value.attackLevel, 1 + baseCC),
      asControlChanges(value.release, 4 + baseCC),
      asControlChanges(value.sustain, 3 + baseCC),
      asControlChanges(value.offset, 5 + baseCC)
    )

  def asControlChanges(value: Patch): Seq[ControlChange] =
    Seq(
      asControlChanges(value.dco2Env2Step, 24),
      asControlChanges(value.arpeggio, 23),
      asControlChanges(value.filterFrequency1, 21),
      asControlChanges(value.filterWidthFrequency2, 22),
      asControlChanges(value.variousModes, 25),
      asControlChanges(value.mixingStructure, 20)
    ) ++
    asControlChanges(value.dco1, 26) ++
    asControlChanges(value.dco2, 102) ++
    asControlChanges(value.env1, 108) ++
    asControlChanges(value.env2, 114)

}