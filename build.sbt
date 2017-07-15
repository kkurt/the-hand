name := "the-hand"

organization := "com.gilt"

crossScalaVersions := Seq("2.10.6", "2.11.11", "2.12.1")

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.3" % "test"
)

resolvers ++= Seq(
  "Sonatype.org Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype.org Releases" at "http://oss.sonatype.org/service/local/staging/deploy/maven2"
)

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "/service/local/staging/deploy/maven2")
}

pomIncludeRepository := { _ => false }

licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))

homepage := Some(url("https://github.com/gilt/the-hand"))

pomExtra := (
  <scm>
    <url>git@github.com:gilt/the-hand.git</url>
    <connection>scm:git:git@github.com:gilt/the-hand.git</connection>
  </scm>
  <developers>
    <developer>
      <id>rlmartin</id>
      <name>Ryan Martin</name>
      <url>https://github.com/rlmartin</url>
    </developer>
    <developer>
      <id>myyk</id>
      <name>Myyk Seok</name>
      <url>https://github.com/myyk</url>
    </developer>
  </developers>)
