package models

import scala.slick.driver.MySQLDriver.simple._

case class Article(
	id: Option[Int], 
	name: String
)

object ArticleDAO extends Table[Article]("articles") {
	
	def id = column[Option[Int]]("id")
	def name = column[String]("name")
	def * = id ~ name <> (Article, Article.unapply _)

	val countByName = for { 
  		name <- Parameters[String]
  		a <- ArticleDAO if a.name === name
	} yield a.id.count

}