package webmidi

sealed trait Channel

final case class All() extends Channel

final case class Single(channel: Int) extends Channel
