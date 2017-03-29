package ui

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal("MyApp")
class MyApp extends js.Object {
  val midi: MyMidi = js.native
  val editor: MyEditor = js.native
}
