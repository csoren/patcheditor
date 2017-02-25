import midi.webmidi.{PortState, WebMidi}
import rxscalajs.subjects.ReplaySubject
import rxscalajs.{Observable, Subject}

package object midi {
  private val _enabledSubject = ReplaySubject.withSize[Boolean](1)

  private val _connectedSubject = Subject[midi.webmidi.Event]()

  private val _disconnectedSubject = Subject[midi.webmidi.Event]()

  def enabledObservable: Observable[Boolean] = _enabledSubject

  def connectedObservable: Observable[midi.webmidi.Event] = _connectedSubject

  def disconnectedObservable: Observable[midi.webmidi.Event] = _disconnectedSubject

  _enabledSubject.filter(_ == true).subscribe { _ =>
    WebMidi.addListener(PortState.connected) { _connectedSubject.next }
    WebMidi.addListener(PortState.disconnected) { _disconnectedSubject.next }
  }

  def enable(): Unit =
    WebMidi.enable(sysex = false) { error =>
      if (error.isEmpty)
        _enabledSubject.next(true)
      else
        println(error.get)
    }


}