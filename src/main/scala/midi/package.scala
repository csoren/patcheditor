import midi.webmidi.{PortState, WebMidi}
import rxscalajs.subjects.ReplaySubject
import rxscalajs.{Observable, Subject}
import reactive._

package object midi {
  private val _enabledSubject = ReplaySubject.withSize[Boolean](1)

  private val _connectedSubject = Subject[midi.webmidi.PortEvent]()

  private val _disconnectedSubject = Subject[midi.webmidi.PortEvent]()

  def enabledObservable: Observable[Boolean] = _enabledSubject.publishReplay(1).refCount

  def connectedObservable: Observable[midi.webmidi.PortEvent] = _connectedSubject.publishReplay(1).refCount

  def disconnectedObservable: Observable[midi.webmidi.PortEvent] = _disconnectedSubject.publishReplay(1).refCount

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
