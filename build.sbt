// Turn this project into a Scala.js project by importing these settings
enablePlugins(ScalaJSPlugin)

name := "patcheditor"

version := "1.0"

scalaVersion := "2.12.1"

persistLauncher in Compile := true

persistLauncher in Test := false

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.1",
  "be.doeraene" %%% "scalajs-jquery" % "0.9.1",
  "com.thoughtworks.binding" %%% "dom" % "11.0.0-M1",
  "com.github.lukajcb" %%% "rxscala-js" % "0.13.2"
)

jsDependencies ++= Seq(
  "org.webjars.npm" % "rxjs" % "5.0.1" / "bundles/Rx.min.js" commonJSName "Rx",
  "org.webjars.bower" % "WebMidi" % "2.0.0-rc.4" / "src/webmidi.js"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
