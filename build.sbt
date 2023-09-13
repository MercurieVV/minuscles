ThisBuild / scalaVersion := "2.13.11"
//sonatypeProfileName := "io.github.mercurievv"
ThisBuild / scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _)) => Seq("-Ykind-projector:underscores")
    case Some((2, 12 | 13)) => Seq("-Xsource:3")
  }
}
val commonSettings = Seq(
  organization := "io.github.mercurievv.minuscles",
  crossScalaVersions := Seq("2.13.11", "3.3.1"),
  pgpPassphrase := sys.env.get("GPG_PASSPHRASE").map(_.toArray),
  publishTo := sonatypePublishToBundle.value,
)
lazy val root = (project in file("."))
  .settings(
    name := "root",
    publish := {},
    publishLocal := {},
    publishArtifact := false,
    publishTo := None,
  )
  .aggregate(monocleTuples, conversions)

lazy val monocleTuples = (project in file("modules/tuples/plens"))
  .settings(commonSettings)
  .settings(
    name := "tuples_plens",
    version := "0.1.3",
    isSnapshot := false,
    description := "Micro library to modify tuples elements and their types",
    Compile / sourceGenerators += Def.task {
      val outputFile = (Compile / sourceManaged).value / "demo" / "implicits.scala"
      IO.write(outputFile, _root_.io.github.mercurievv.minuscles.tuples.plens.generator.TuplesPLensGenerator.gen(6))
      streams.value.log.info("Generated source: " + outputFile)
      Seq(outputFile)
    }.taskValue,
  )
  .settings(
    libraryDependencies += "dev.optics" %% "monocle-core" % "3.2.0"
  )

lazy val conversions = (project in file("modules/conversions"))
  .settings(commonSettings)
  .settings(
    name := "conversions",
    version := "0.1.0",
    isSnapshot := false,
    description := "Micro library to modify things generic way",
  )
  .settings(
    libraryDependencies += "org.typelevel" %% "cats-core"     % "2.9.0",
    libraryDependencies += "dev.optics"    %% "monocle-macro" % "3.2.0" % Test,
  )
  .dependsOn(monocleTuples % "compile->test")
