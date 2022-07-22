val scala3Version = "3.1.3"

lazy val root = project
  .in(file("."))
  .settings(
    name         := "prelude-of-purity",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.3.14",
      "dev.zio"       %% "zio-prelude" % "1.0.0-RC15"
    )
  )
