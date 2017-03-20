package midi.webmidi

import scala.scalajs.js.|

sealed trait Channel

final case class All() extends Channel

final case class Single(channel: Int) extends Channel

object Channel {
  def asJs(channel: Channel): Int | String =
    channel match {
      case All() => "all"
      case Single(n) => n
    }

  implicit final class ChannelExt(val channel: Channel) extends AnyVal {
    def asJs: Int | String = Channel.asJs(channel)
  }

}