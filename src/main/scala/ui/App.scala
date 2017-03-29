package ui

class App(ui: facade.MyApp) {
  val midi = new Midi(ui.midi)
  val editor = new Editor(ui.editor)
}
