val scala3Ver = "3.4.2"

ThisBuild / tlBaseVersion := "0.1"
ThisBuild / licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / startYear := Some(2025)
ThisBuild / scalaVersion := "2.13.16"
ThisBuild / crossScalaVersions := Seq("2.13.16", scala3Ver)
ThisBuild / tlSitePublishBranch := Some("main")
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("11"))
//ThisBuild / tlCiReleaseTags := false

addCommandAlias("prePush", "; headerCheckAll; scalafmtCheckAll; scalafmtSbtCheck; +test; docs/mdoc")

val commonSettings = Seq(
  organization := "io.github.mercurievv.minuscles",
  pgpPassphrase := sys.env.get("GPG_PASSPHRASE").map(_.toArray),
  publishTo := sonatypePublishToBundle.value,
  sonatypeProfileName := "io.github.mercurievv",
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((3, _))  => Seq("-Xmax-inlines:64", "-Werror")
      case Some((2, 13)) => Seq("-Xsource:3", "-Xfatal-warnings")
    }
  },
  headerLicense := Some(HeaderLicense.ALv2("2025", "Viktors Kalinins")),
  tlCiReleaseTags := false,
  tlCiMimaBinaryIssueCheck := false,
  mimaFailOnNoPrevious := false,
  mimaReportBinaryIssues := false,
  publish / skip := isAlreadyPublished.value,
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
  .aggregate(monocleTuples, conversions, fieldsNames, shapeless3typeclasses, tuplesTransformers, opaques, docs)

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
  .settings(NoPublishPlugin.projectSettings)

lazy val tuplesTransformers = (project in file("modules/tuples/transformers"))
  .settings(commonSettings)
  .settings(
    name := "tuples-transformers",
    version := "0.1.0",
    isSnapshot := false,
    description := "Micro library to modify tuples elements and their types",
    crossScalaVersions := Seq(scala3Ver),
    scalaVersion := scala3Ver,
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
  .settings(NoPublishPlugin.projectSettings)

lazy val shapeless3typeclasses = (project in file("modules/shapeless3-typeclasses"))
  .settings(commonSettings)
  .settings(
    name := "shapeless3-typeclasses",
    version := "0.1.0",
    isSnapshot := false,
    description := "Typeclasses for shapeless 3",
    crossScalaVersions := Seq(scala3Ver),
    scalaVersion := scala3Ver,
  )
  .settings(
    libraryDependencies += "org.typelevel" %% "shapeless3-deriving" % "3.4.3",
    libraryDependencies += "org.typelevel" %% "cats-core"           % "2.13.0",
  )
  .settings(NoPublishPlugin.projectSettings)

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
  .settings(NoPublishPlugin.projectSettings)

lazy val opaques = (project in file("modules/opaques"))
  .settings(commonSettings)
  .settings(
    name := "opaques",
    version := "0.1.1",
    isSnapshot := false,
    description := "Micro library to create opaque types and companion objects generic way",
    crossScalaVersions := Seq(scala3Ver),
    scalaVersion := scala3Ver,
  )

lazy val docs = project
  .in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
  .dependsOn(tuplesTransformers, opaques)
  .settings(
    crossScalaVersions := Seq(scala3Ver),
    scalaVersion := scala3Ver,
    tlSiteIsTypelevelProject := Some(TypelevelProject.Affiliate),
    mdocVariables := Map(
      "TUPLES_TRANSFORMERS_VERSION" -> tuplesTransformers.project./(version).value,
      "OPAQUES_VERSION"             -> opaques.project./(version).value,
    ),
  )
  .settings(NoPublishPlugin.projectSettings)

/*
lazy val docs = project // new documentation project
  .in(file("minuscles-docs")) // important: it must not be docs/
  .dependsOn(tuplesTransformers)
  .enablePlugins(MdocPlugin)
  .settings(
    crossScalaVersions := Seq(scala3Ver),
    scalaVersion := scala3Ver,
    mdocVariables := Map(
      "VERSION" -> tuplesTransformers.project./(version).value
    )
  )
 */

import scala.sys.process._

lazy val isAlreadyPublished = Def.setting {
  val org  = organization.value
  val name = moduleName.value // includes _2.13 / _3 suffix for cross-compiled artifacts
  val ver  = version.value
  if (isSnapshot.value) false
  else isPublished(org, name, ver)
}

def isPublished(organization: String, name: String, version: String): Boolean = {
  val url = s"https://repo1.maven.org/maven2/${organization.replace('.', '/')}/$name/$version/$name-$version.pom"
  try {
    val exitCode = Seq("curl", "--head", "--fail", url).!
    println(s"exitCode = ${exitCode}")
    exitCode == 0
  } catch {
    case _: Throwable => false
  }
}
