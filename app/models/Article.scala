package models

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.{GetResult, StaticQuery => Q}

import utils._

case class Article(
	id: Option[Int], 
	name: String
)

object ArticleDAO extends Table[Article]("articles") with DatabaseAccess {
	
	def id = column[Option[Int]]("id")
	def name = column[String]("name")
	def * = id ~ name <> (Article, Article.unapply _)

	val countByName = for { 
  		name <- Parameters[String]
  		a <- ArticleDAO if a.name === name
	} yield a.id.count


	def clean = {
		database.withSession { implicit session:Session =>
            Query(ArticleDAO).delete
        }
	}

	def insertWithRead() = {
		database.withSession { implicit session:scala.slick.driver.MySQLDriver.simple.Session =>
            for (i <- 0 to 1000) {

                if ((Query(ArticleDAO).filter(_.name === "With - " + (i%500).toString).list.length) == 0) {
                    ArticleDAO.insert(Article(None, "With - " + (i % 500).toString))
                }
            }
        }	
	}

	def insertWithoutRead() = {
        database.withSession { implicit session:Session =>
            for (i <- 0 to 1000) {
                try {
                    ArticleDAO.insert(Article(None, "Without - " + (i % 500).toString))
                } catch {
                    case _:Throwable => Unit
                }
            }
        }
	}

	def insertRaw() = {
		database.withSession { implicit session:Session =>
	    	for (i <- 0 to 1000) {
	    		(Q.u + "insert ignore into articles (`name`) values (\"Without - " + (i % 500).toString + "\")").execute
            }
        }	
	}
}