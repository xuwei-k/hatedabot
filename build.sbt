name := "hatedabot"

version := "0.1-SNAPSHOT"

resolvers ++= Seq(
  "twitter repository"   at "http://maven.twttr.com"
 ,"twitter4j repository" at "http://twitter4j.org/maven2"
 ,"sonatype" at "https://oss.sonatype.org/content/repositories/releases"
)

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "2.2.5"
 ,"com.twitter" %% "util-eval" % "3.0.0"
 ,"org.specs2"  %% "specs2" % "1.8.2" % "test"
)

scalacOptions += "-deprecation"

seq(assemblySettings: _*)

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

