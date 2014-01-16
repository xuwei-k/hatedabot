name := "hatedabot"

version := "0.1-SNAPSHOT"

organization := "com.github.xuwei-k"

licenses += ("MIT License",url("https://github.com/xuwei-k/hatedabot/blob/master/LICENSE.txt"))

homepage := Some(url("https://github.com/xuwei-k/hatedabot"))

resolvers += Opts.resolver.sonatypeReleases

resolvers ++= Seq(
 "twitter4j" at "http://twitter4j.org/maven2"
)

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "3.0.5"
 ,"com.twitter" %% "util-eval" % "6.3.5"
 ,"org.specs2"  %% "specs2" % "1.14" % "test"
)

scalacOptions += "-deprecation"

assemblySettings

AssemblyKeys.jarName in AssemblyKeys.assembly <<= (name, version).map{ (name, version) =>
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
  <x>{name}-{df.format(new java.util.Date)}-{version}.jar</x>.text
}

publishTo := sys.env.get("MAVEN_DIRECTORY").map{ dir =>
  Resolver.file("gh-pages",file(dir))(Patterns(true, Resolver.mavenStyleBasePattern))
}

