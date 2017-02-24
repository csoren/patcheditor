import rxscalajs.{Observable, Subject}
import rxscalajs.subjects.ReplaySubject
import webmidi.{PortState, WebMidi}

object Midi {
  private val _enabledSubject = ReplaySubject.withSize[Boolean](1)

  private val _connectedSubject = Subject[webmidi.Event]()

  private val _disconnectedSubject = Subject[webmidi.Event]()

  def enabledObservable: Observable[Boolean] = _enabledSubject

  def connectedObservable: Observable[webmidi.Event] = _connectedSubject

  def disconnectedObservable: Observable[webmidi.Event] = _disconnectedSubject

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
