ThisBuild / scalaVersion := "2.13.14"
ThisBuild / crossScalaVersions := Seq("2.13.14", "3.4.2")

val commonSettings = Seq(
  scalaVersion := "2.13.14",
  organization := "io.github.mercurievv.minuscles",
  crossScalaVersions := Seq("2.13.14", "3.4.2"),
  pgpPassphrase := sys.env.get("GPG_PASSPHRASE").map(_.toArray),
  publishTo := sonatypePublishToBundle.value,
  sonatypeProfileName := "io.github.mercurievv",
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((3, _))  => Seq("-Ykind-projector:underscores")
      case Some((2, 13)) => Seq("-Xsource:3")
    }
  },
)
lazy val root = (project in file("."))
  .settings(
    name := "minuscles",
    publish := {},
    publishLocal := {},
    publish / skip := true,
    publishArtifact := false,
    publishTo := None,
  )
  .aggregate(monocleTuples, conversions, fieldsNames)

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

lazy val fieldsNames = (project in file("modules/fields-names"))
  .settings(commonSettings)
  .settings(
    name := "fields_names",
    version := "0.1.0",
    isSnapshot := false,
    description := "Micro library generate object containing its fields names",
  )
  .settings(
    libraryDependencies ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 13)) => Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value)
        case _             => Seq.empty
      }
    },
    libraryDependencies ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 13)) => List("com.softwaremill.magnolia1_2" %% "magnolia" % "1.1.10")
        case Some((3, _))  => List("com.softwaremill.magnolia1_3" %% "magnolia" % "1.3.7")
      }
    },
    libraryDependencies += "org.scalameta" %% "munit-scalacheck" % "0.7.29" % Test,
  )

lazy val conversions = (project in file("modules/conversions"))
  .settings(commonSettings)
  .settings(
    name := "conversions",
    version := "0.1.1",
    isSnapshot := false,
    description := "Micro library to modify things generic way",
  )
  .settings(
    libraryDependencies += "org.typelevel" %% "cats-core"     % "2.9.0",
    libraryDependencies += "dev.optics"    %% "monocle-macro" % "3.2.0" % Test,
  )
  .dependsOn(monocleTuples)
