ThisBuild / organization := "io.github.mercurievv.minuscles"
ThisBuild / organizationHomepage := Some(url("https://github.com/MercurieVV/"))
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeProfileName := "io.github.mercurievv"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/MercurieVV/minuscles"),
    "scm:git@github.com:MercurieVV/minuscles.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id    = "MercurieVV",
    name  = "Viktors Kalinins",
    email = "mercureivv@gmail.com",
    url   = url("https://github.com/MercurieVV/")
  )
)

//ThisBuild / description := "Describe your project here..."
ThisBuild / licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / homepage := Some(url("https://github.com/MercurieVV/minuscles"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (false) Some("snapshots" at nexus + "content/repositories/snapshots/")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2/")
}

ThisBuild / publishMavenStyle := true

ThisBuild / versionScheme := Some("early-semver")

ThisBuild / pgpPassphrase := sys.env.get("GPG_PASSPHRASE").map(_.toArray)
