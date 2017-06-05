// Turn this project into a Scala.js project by importing these settings
enablePlugins(ScalaJSPlugin)

name := "patcheditor"

version := "1.0"

scalaVersion := "2.12.2"

scalacOptions ++= Seq("-feature", "-P:scalajs:sjsDefinedByDefault")

scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.1",
  "com.github.lukajcb" %%% "rxscala-js" % "0.13.2"
)

jsDependencies ++= Seq(
  "org.webjars.npm" % "rxjs" % "5.0.1" / "bundles/Rx.min.js" commonJSName "Rx",
  "org.webjars.npm" % "webmidi" % "2.0.0-rc.5" / "webmidi.min.js"
)
