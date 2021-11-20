lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "ch.epfl.scala",
      scalaVersion := "2.13.6"
    )),
    name := "depviz"
  )

lazy val frontEnd = (project in file("front-end"))
  .settings(
    name := "depviz-front-end"
  )