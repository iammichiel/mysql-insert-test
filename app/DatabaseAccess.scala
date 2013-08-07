package utils

import scala.slick.driver.MySQLDriver.simple._

import play.api.db._
import play.api.Play.current

trait DatabaseAccess {
	
	lazy val database = Database.forDataSource(DB.getDataSource())
}