import sbt._
import sbt.classpath._
import Keys._
import Process._
import System._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization        := "com.promindis",
    version             := "0.1-SNAPSHOT",
    scalaVersion        := "2.9.1",
    scalacOptions       := Seq("-unchecked", "-deprecation")
  )
}


object Resolvers {
  val typesafeReleases = "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
  val scalaToolsReleases = "Scala-Tools Maven2 Releases Repository" at "http://scala-tools.org/repo-releases"
  val scalaToolsSnapshots = "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
}

object TestDependencies {
  val specs2Version = "1.7.1"
  val testDependencies = "org.specs2" %% "specs2" % specs2Version % "test"
}

object AKKADependencies {
  val akkaVersion = "1.3-RC6"
  val actorDependencies = "se.scalablesolutions.akka" % "akka-actor" % akkaVersion
  val stmDependencies = "se.scalablesolutions.akka" % "akka-stm" % akkaVersion
}

object SwingDependencies {
  val swingDependencies = "org.scala-lang" % "scala-swing" % "2.9.1"
}


object MainBuild extends Build {
  import Resolvers._
  import TestDependencies._
  import AKKADependencies._
  import BuildSettings._
  import SwingDependencies._

  lazy val algorithms = Project(
    "Snake",
    file("."),
    settings = buildSettings ++ Seq(resolvers += typesafeReleases) ++  
              Seq (libraryDependencies ++= Seq(testDependencies, actorDependencies, stmDependencies, swingDependencies))
  )

}
