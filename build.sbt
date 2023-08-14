ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

val commonSettings = Seq(
  organization := "io.github.mercurievv.minuscles",
  crossScalaVersions := Seq("2.13.11", "3.3.0"),
  pgpPassphrase := sys.env.get("GPG_PASSPHRASE").map(_.toArray),
)
lazy val root = (project in file("."))
  .settings(
    name := "root",
    publish := {},
    publishLocal := {},
    publishArtifact := false,
  ).aggregate(monocleTuples)

lazy val monocleTuples = (project in file("modules/tuples/plens"))
  .settings(commonSettings)
  .settings(
    name := "tuples_plens",
    version := "0.1.0",
    isSnapshot := false,
    description := "Micro library to modify tuples elements and their types",
    Compile / sourceGenerators += Def.task {
      val outputFile = (Compile / sourceManaged).value / "demo" / "implicits.scala"
      IO.write(outputFile, _root_.io.github.mercurievv.minuscles.tuples.plens.generator.TuplesPLensGenerator.gen(6))
      streams.value.log.info("Generated source: " + outputFile)
      Seq(outputFile)
    }.taskValue
  ).settings(
  libraryDependencies += "dev.optics" %% "monocle-core" % "3.2.0"
)


