package controllers

import play.api._
import play.api.mvc._

import scala.slick.driver.MySQLDriver.simple._

import utils._
import models._

case class TestResult(
  before: Long,
  after: Long
)

object Application extends Controller with DatabaseAccess {

    def index = Action {
        Ok(views.html.index())
    }

    def test() = Action {

        // Cleaning the database
        database.withSession { implicit session:scala.slick.driver.MySQLDriver.simple.Session =>
            Query(ArticleDAO).delete
        }

        var beforeWithRead:Long = 0L
        var afterWithRead:Long = 0L
        var beforeWithoutRead:Long = 0L
        var afterWithoutRead:Long = 0L

        database.withSession { implicit session:scala.slick.driver.MySQLDriver.simple.Session =>
            beforeWithRead = System.currentTimeMillis()
                for (i <- 0 to 1000) {

                    if ((Query(ArticleDAO).filter(_.name === "With - " + (i%500).toString).list.length) == 0) {
                        ArticleDAO.insert(Article(None, "With - " + (i % 500).toString))
                    }
                }
            afterWithRead = System.currentTimeMillis()
        }

        // Cleaning the database
        database.withSession { implicit session:scala.slick.driver.MySQLDriver.simple.Session =>
            Query(ArticleDAO).delete
        }

        // Inserting just ignoring the errors
        database.withSession { implicit session:scala.slick.driver.MySQLDriver.simple.Session =>
            beforeWithoutRead = System.currentTimeMillis()
            for (i <- 0 to 1000) {
                try {
                    ArticleDAO.insert(Article(None, "Without - " + (i % 500).toString))
                } catch {
                    case _:Throwable => Unit
                }
            }
            afterWithoutRead = System.currentTimeMillis()
        }

        Ok(views.html.result(
            TestResult(beforeWithRead, afterWithRead),
            TestResult(beforeWithoutRead, afterWithoutRead)
        ))
    }
}