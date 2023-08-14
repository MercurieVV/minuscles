ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

val commonSettings = Seq(
  organization := "com.github.mercurievv.minuscles",
  crossScalaVersions := Seq("2.13.11", "3.3.0"),
)
lazy val root = (project in file("."))
  .settings(
    name := "root",
  ).aggregate(monocleTuples)

lazy val monocleTuples = (project in file("modules/tuples/plens"))
  .settings(commonSettings)
  .settings(
    name := "tuples_plens",
    version := "0.1.0",
    description := "Micro library to modify tuples elements and their types",
    Compile / sourceGenerators += Def.task {
      val outputFile = (Compile / sourceManaged).value / "demo" / "implicits.scala"
      IO.write(outputFile, com.github.mercurievv.minuscles.tuples.plens.generator.TuplesPLensGenerator.gen(6))
      streams.value.log.info("Generated source: " + outputFile)
      Seq(outputFile)
    }.taskValue
  ).settings(
  libraryDependencies += "dev.optics" %% "monocle-core" % "3.2.0"
)


