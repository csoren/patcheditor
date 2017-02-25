package midi.webmidi

object PortState extends Enumeration {
  type PortState = Value

  val connected = Value
  val disconnected = Value
}
