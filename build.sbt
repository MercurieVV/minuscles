ThisBuild / tlBaseVersion := "0.1"
ThisBuild / licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / startYear := Some(2025)
//ThisBuild / headerLicense := Some(HeaderLicense.ALv2("2025", "Viktors Kalinins"))
ThisBuild / scalaVersion := "2.13.16"
ThisBuild / crossScalaVersions := Seq("2.13.16", "3.4.2")
ThisBuild / tlSitePublishBranch := Some("main")
//ThisBuild / tlCiReleaseTags := false

val commonSettings = Seq(
  scalaVersion := "2.13.16",
  organization := "io.github.mercurievv.minuscles",
  startYear := Some(2025),
  crossScalaVersions := Seq("2.13.16", "3.4.2"),
  pgpPassphrase := sys.env.get("GPG_PASSPHRASE").map(_.toArray),
  publishTo := sonatypePublishToBundle.value,
  sonatypeProfileName := "io.github.mercurievv",
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((3, _))  => Seq("-Xmax-inlines:64", "-Werror")
      case Some((2, 13)) => Seq("-Xsource:3", "-Xfatal-warnings")
    }
  },
  licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0")),
  headerLicense := Some(HeaderLicense.ALv2("2025", "Viktors Kalinins")),
  //tlCiReleaseTags := false,
  //tlCiMimaBinaryIssueCheck := false,
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
  .aggregate(monocleTuples, conversions, fieldsNames, shapeless3typeclasses, tuplesTransformers, docs)

//todo use tuplesTransformers. This lib should apply tuplesTransformers for monocle only
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

lazy val tuplesTransformers = (project in file("modules/tuples/transformers"))
  .settings(commonSettings)
  .settings(
    name := "tuples_transformers",
    version := "0.1.0",
    isSnapshot := false,
    description := "Micro library to modify tuples elements and their types",
    crossScalaVersions := Seq("3.4.2"),
    scalaVersion := "3.4.2",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "munit-cats-effect" % "2.0.0"  % Test,
      "org.typelevel" %% "discipline-munit"  % "2.0.0"  % Test,
      "org.typelevel" %% "cats-laws"         % "2.13.0" % Test,
    ),
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

lazy val shapeless3typeclasses = (project in file("modules/shapeless3-typeclasses"))
  .settings(commonSettings)
  .settings(
    name := "shapeless3-typeclasses",
    version := "0.1.0",
    isSnapshot := false,
    description := "Typeclasses for shapeless 3",
    crossScalaVersions := Seq("3.4.2"),
    scalaVersion := "3.4.2",
  )
  .settings(
    libraryDependencies += "org.typelevel" %% "shapeless3-deriving" % "3.4.3",
    libraryDependencies += "org.typelevel" %% "cats-core"           % "2.13.0",
  )

lazy val conversions = (project in file("modules/conversions"))
  .settings(commonSettings)
  .settings(
    name := "conversions",
    version := "0.1.1",
    isSnapshot := false,
    description := "Micro library to modify things generic way",
  )
  .settings(NoPublishPlugin.projectSettings)
  .settings(
    libraryDependencies += "org.typelevel" %% "cats-core"     % "2.9.0",
    libraryDependencies += "dev.optics"    %% "monocle-macro" % "3.2.0" % Test,
  )
  .dependsOn(monocleTuples)

lazy val docs = project
  .in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
  .dependsOn(tuplesTransformers)
  .settings(
    crossScalaVersions := Seq("3.4.2"),
    scalaVersion := "3.4.2",
    tlSiteIsTypelevelProject := Some(TypelevelProject.Affiliate),
    mdocVariables := Map(
      "VERSION" -> tuplesTransformers.project./(version).value
    ),
  )

/*
lazy val docs = project // new documentation project
  .in(file("minuscles-docs")) // important: it must not be docs/
  .dependsOn(tuplesTransformers)
  .enablePlugins(MdocPlugin)
  .settings(
    crossScalaVersions := Seq("3.4.2"),
    scalaVersion := "3.4.2",
    mdocVariables := Map(
      "VERSION" -> tuplesTransformers.project./(version).value
    )
  )
 */
