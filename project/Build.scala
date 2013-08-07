import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "MysqlTest"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
        // Add your project dependencies here,
        jdbc,
        "com.typesafe.slick" %% "slick"                %  "1.0.0",
        "mysql"              %  "mysql-connector-java" %  "5.1.25"
    )


    val main = play.Project(appName, appVersion, appDependencies).settings(
        // Add your own project settings here
    )
}