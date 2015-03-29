name := "hatedabot"

version := "0.1-SNAPSHOT"

organization := "com.github.xuwei-k"

licenses += ("MIT License",url("https://github.com/xuwei-k/hatedabot/blob/master/LICENSE.txt"))

homepage := Some(url("https://github.com/xuwei-k/hatedabot"))

resolvers += Opts.resolver.sonatypeReleases

resolvers ++= Seq(
 "twitter4j" at "http://twitter4j.org/maven2"
)

scalaVersion := "2.11.6"

val twitter4jVersion = "4.0.3"

libraryDependencies ++= (
  ("org.twitter4j" % "twitter4j-core" % twitter4jVersion) ::
  ("org.scala-lang" % "scala-compiler" % scalaVersion.value) ::
  ("io.argonaut" %% "argonaut" % "6.1-M5" exclude("org.scala-lang", "scala-compiler")) ::
  ("org.specs2"  %% "specs2" % "2.4" % "test") ::
  ("org.scalaj"  %% "scalaj-http" % "0.3.16") ::
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
  "-Ywarn-unused" ::
  "-Ywarn-unused-import" ::
  Nil
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
