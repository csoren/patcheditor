package webmidi

object PortStatus extends Enumeration {
  type PortStatus = Value

  val pending = Value
  val open = Value
  val closed = Value
}
