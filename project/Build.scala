import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "GoogleOauth"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "commons-codec"                        %  "commons-codec"                % "1.6",
    //"com.google.code.google-collections" %  "google-collect"               % "snapshot-20080530" withSources,
    //"com.google.api-client"              %  "google-api-client"            % "1.14.1-beta" withSources,
    //"com.google.apis"                    %  "google-api-services-tasks"    % "v1-rev16-1.15.0-rc" withSources,
    //"com.google.oauth-client"            %  "google-oauth-client"          % "1.15.0-rc" withSources,
    //"com.google.http-client"             %  "google-http-client-jackson2"  % "1.11.0-beta" withSources,
    //"com.google.apis"                    %  "google-api-services-oauth2"   % "v2-rev39-1.15.0-rc" withSources,
    "com.google.oauth-client"            %  "google-oauth-client-java6"    % "1.15.0-rc" withSources
    //"com.google.oauth-client"            %  "google-oauth-client-jetty"    % "1.15.0-rc" withSources
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
