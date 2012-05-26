name := "hatedabot"

version := "0.1-SNAPSHOT"

organization := "com.github.xuwei-k"

licenses += ("MIT License",url("https://github.com/xuwei-k/hatedabot/blob/master/LICENSE.txt"))

homepage := Some(url("https://github.com/xuwei-k/hatedabot"))

externalResolvers ~= { _.filterNot{_.name.contains("Scala-Tools")} }

resolvers ++= Seq(
  "https://oss.sonatype.org/content/repositories/releases"
 ,"http://maven.twttr.com"
 ,"http://twitter4j.org/maven2"
).map{u => u at u}

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "2.2.5"
 ,"com.twitter" %% "util-eval" % "3.0.0"
 ,"org.specs2"  %% "specs2" % "1.9" % "test"
)

scalacOptions += "-deprecation"

assemblySettings

addCompilerPlugin("org.scala-tools.sxr" % "sxr_2.9.0" % "0.2.7")

scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

initialCommands in console :=
  Seq(
    "hatedabot","com.twitter.conversions.time","com.twitter.util"
  ).map{p =>
    "import " + p + "._;"
  }.mkString("\n")

AssemblyKeys.jarName in AssemblyKeys.assembly <<= (name,version){(name,version) =>
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
  <x>{name}-{df.format(new java.util.Date)}-{version}.jar</x>.text
}

publishTo := sys.env.get("MAVEN_DIRECTORY").map{ dir =>
  Resolver.file("gh-pages",file(dir))(Patterns(true, Resolver.mavenStyleBasePattern))
}

