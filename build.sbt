val commonSettings = Seq(
  scalaVersion := "2.13.11",
  organization := "io.github.mercurievv.minuscles",
  crossScalaVersions := Seq("2.13.11", "3.3.1"),
  pgpPassphrase := sys.env.get("GPG_PASSPHRASE").map(_.toArray),
  publishTo := sonatypePublishToBundle.value,
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((3, _))       => Seq("-Ykind-projector:underscores", "-source:future")
      case Some((2, 12 | 13)) => Seq("-Xsource:3")
    }
  },
  libraryDependencies ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 12 | 13)) =>
        List(compilerPlugin("com.olegpy" % "better-monadic-for_2.13" % "0.3.1"))
      case _ => Nil
    }
  },
)
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
lazy val root = (project in file("."))
  .settings(
    name := "root",
    publish := {},
    publishLocal := {},
    publishArtifact := false,
    publishTo := None,
  )
  .aggregate(monocleTuples, conversions, context4log4cats)

lazy val context4log4cats = (project in file("modules/context4log4cats"))
  .settings(commonSettings)
  .settings(
    name := "context4log4cats",
    version := "0.1.0",
    isSnapshot := false,
    description := "Micro library to add context to loggets (Log4cats)",
  )
  .settings(
    libraryDependencies += "org.typelevel"       %% "log4cats-core"    % "2.7.0",
    libraryDependencies += "org.typelevel"       %% "log4cats-testing" % "2.7.0" % Test,
    libraryDependencies += "com.disneystreaming" %% "weaver-cats"      % "0.8.4" % Test,
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
    libraryDependencies += "com.disneystreaming" %% "weaver-scalacheck" % "0.8.4" % Test,
  )

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
    version := "0.1.1",
    isSnapshot := false,
    description := "Micro library to modify things generic way",
  )
  .settings(
    libraryDependencies += "org.typelevel" %% "cats-core"     % "2.9.0",
    libraryDependencies += "dev.optics"    %% "monocle-macro" % "3.2.0" % Test,
  )
  .dependsOn(monocleTuples)
