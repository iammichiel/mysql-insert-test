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

        // With read
        ArticleDAO.clean
        val beforeWithRead = System.currentTimeMillis()
        ArticleDAO.insertWithRead
        val afterWithRead = System.currentTimeMillis()

        // Without read
        ArticleDAO.clean
        val beforeWithoutRead = System.currentTimeMillis()
        ArticleDAO.insertWithoutRead
        val afterWithoutRead = System.currentTimeMillis()

        // Raw inserts
        ArticleDAO.clean
        val beforeRaw = System.currentTimeMillis()
        ArticleDAO.insertRaw
        val afterRaw = System.currentTimeMillis()

        Ok(views.html.result(
            TestResult(beforeWithRead, afterWithRead),            
            TestResult(beforeWithoutRead, afterWithoutRead),
            TestResult(beforeRaw, afterRaw)
        ))
    }
}