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
  "com.thoughtworks.binding" %%% "dom" % "11.0.0-M1"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
