import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "GoogleOauthPlay"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "commons-codec"           %  "commons-codec"             % "1.6",
    "com.google.oauth-client" %  "google-oauth-client-java6" % "1.15.0-rc" withSources
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    organization := "com.micronautics",

    scalaVersion := "2.10.2",

    scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.7", "-unchecked",
      "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint"),

    javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.7", "-target", "1.7", "-g:vars"),

    resolvers ++= Seq(
      "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"
    ),

    initialCommands := """
                         |""".stripMargin

  )

}
