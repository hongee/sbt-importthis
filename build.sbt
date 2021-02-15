scalaVersion := "2.13.3"

name := "import-this"
organization := "com.github.hongee"
version := "1.0"

enablePlugins(NativeImagePlugin)

Compile / mainClass := Some("com.github.hongee.importthis.Main")

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "4.0.0",
  "org.typelevel" %% "cats-effect" % "2.3.1",
  "com.softwaremill.sttp.client3" %% "core" % "3.1.0",
  "com.softwaremill.sttp.client3" %% "httpclient-backend-fs2" % "3.1.0",
  "org.jsoup" % "jsoup" % "1.13.1",
  "com.github.kovszilard" %% "smenu" % "0.1.0"
)

nativeImageOptions ++= Seq(
  "-H:+ReportExceptionStackTraces",
  s"-H:ResourceConfigurationFiles=${target.value.absolutePath}/native-image-configs/resource-config.json",
  "-H:Log=registerResource:",
  "--no-fallback", "--allow-incomplete-classpath",
  "--report-unsupported-elements-at-runtime",
  "--enable-https"
)
