lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        organization := "ch.epfl.scala",
        scalaVersion := "2.13.4",
        scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"
      )
    ),
    name := "depviz"
  )
  .aggregate(frontEnd, backEnd)

lazy val scalaJsTopDir =
  settingKey[File]("Scala JS top dir, used for the server")
lazy val scalaJsAssets = settingKey[File]("Assets folder")
lazy val frontEnd = (project in file("front-end"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "depviz-front-end",
    scalaJSUseMainModuleInitializer := true,
    scalaJsTopDir := baseDirectory.value / "src" / "top",
    scalaJsAssets := scalaJsTopDir.value / "assets" / "js",
    Compile / fullLinkJS / scalaJSLinkerOutputDirectory := {
      scalaJsAssets.value / "full-opt"
    },
    // TODO: why is scalaJSLinkerOutputDirectory path unused?
    Compile / fastOptJS / artifactPath := {
      scalaJsAssets.value / "fast-opt" / "main.js"
    },
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.1.0"
    )
  )

lazy val buildInfoSettings = Def.settings(
  buildInfoKeys := Seq[BuildInfoKey](
    name,
    version,
    scalaVersion,
    sbtVersion,
    "assetsPath" -> Some((frontEnd / scalaJsTopDir).value)
  ),
  buildInfoPackage := "depviz"
)
lazy val backEnd = (project in file("back-end"))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "depviz-back-end",
    reStart / mainClass := Some("depviz.Main"),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % V.http4s,
      "org.http4s" %% "http4s-ember-client" % V.http4s,
      "org.http4s" %% "http4s-circe" % V.http4s,
      "org.http4s" %% "http4s-dsl" % V.http4s,
      "com.lihaoyi" %% "scalatags" % V.scalaTags,
      "ch.qos.logback" % "logback-classic" % V.logback,
      //
      "org.scalameta" %% "munit" % V.munit % Test,
      "org.typelevel" %% "munit-cats-effect-3" % V.munitCatsEffect % Test
    )
  )
  .settings(buildInfoSettings)
