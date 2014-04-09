package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.NoSuchElementException

case class Purchaser(id: Long, name: String)

object Purchaser {
  val purchaser = {
    get[Long]("id") ~
      get[String]("name") map {
      case id~name => Purchaser(id, name)
    }
  }

  def allPurchasers() = {
    DB.withConnection { implicit c =>
      SQL("select * from purchasers").as(purchaser *)
    }
  }

  def getPurchaser(id: Long) = {
    DB.withConnection { implicit c =>
      SQL("select * from purchasers where id = {id}").on(
        'id -> id
      ).as(purchaser *)
    }
  }

  def getPurchaserId(name: String): Option[Long] = {
    try {
      Some(DB.withConnection { implicit c =>
        SQL("select id from purchasers where upper(name) = upper({name})").on(
          'name -> name
        ).apply().head[Long]("id")
      })
    } catch {
      case nse: NoSuchElementException => None
    }
  }

  def addPurchaser(name: String) {
    //Add the purchaser only if another purchaser with the same name doesn't already exist
    getPurchaserId(name) match {
      case Some(s) => {}

      case None => DB.withConnection { implicit c =>
        SQL("insert into purchasers (name) values ({name})").on(
          'name -> name
        ).executeUpdate()
      }
    }
  }

  def deletePurchaser(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from purchasers where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }
}