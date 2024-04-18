val scala3Version = "3.4.2-RC1-bin-20240314-3d5cf9c-NIGHTLY"

lazy val root = project
  .in(file("."))
  .settings(
    name                                   := "trying-scala3-dot-4",
    version                                := "0.1.0-SNAPSHOT",
    scalaVersion                           := scala3Version,
    //scalacOptions += "-experimental",
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
  )
