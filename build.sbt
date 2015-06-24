name := "hatedabot"

version := "0.1-SNAPSHOT"

organization := "com.github.xuwei-k"

licenses += ("MIT License",url("https://github.com/xuwei-k/hatedabot/blob/master/LICENSE.txt"))

homepage := Some(url("https://github.com/xuwei-k/hatedabot"))

resolvers += Opts.resolver.sonatypeReleases

scalaVersion := "2.11.7"

val twitter4jVersion = "4.0.4"

libraryDependencies ++= (
  ("org.twitter4j" % "twitter4j-core" % twitter4jVersion) ::
  ("org.scala-lang" % "scala-compiler" % scalaVersion.value) ::
  ("io.argonaut" %% "argonaut" % "6.1-M6") ::
  ("org.specs2"  %% "specs2" % "2.4" % "test") ::
  ("org.scalaj"  %% "scalaj-http" % "0.3.16") ::
  Nil
)

val unusedWarnings = (
  "-Ywarn-unused" ::
  "-Ywarn-unused-import" ::
  Nil
)

scalacOptions ++= (
  "-deprecation" ::
  "-unchecked" ::
  "-Xlint" ::
  "-language:postfixOps" ::
  "-language:existentials" ::
  "-language:higherKinds" ::
  "-language:implicitConversions" ::
  Nil
) ::: unusedWarnings

Seq(Compile, Test).flatMap(c =>
  scalacOptions in (c, console) ~= {_.filterNot(unusedWarnings.toSet)}
)

assemblySettings

AssemblyKeys.jarName in AssemblyKeys.assembly := {
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
  s"${name.value}-${df.format(new java.util.Date)}-twitter4j-${twitter4jVersion}.jar"
}

resourceGenerators in Compile += task(
  Seq(baseDirectory.value / "build.sbt")
)

sourcesInBase := false
