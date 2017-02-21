package object webmidi {
  implicit final class EventExt(val event: Event) {
    def eventType: PortState.PortState = PortState.withName(event._type)
  }

  implicit final class InputExt(val input: Input) {
    def connection: PortStatus.PortStatus = PortStatus.withName(input._connection)
    def state: PortState.PortState = PortState.withName(input._state)
  }
}
