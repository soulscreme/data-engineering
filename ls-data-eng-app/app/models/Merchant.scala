package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.NoSuchElementException

case class Merchant(id: Long, name: String, address: String)

object Merchant {
  val merchant = {
    get[Long]("id") ~
      get[String]("name") ~
      get[String]("address") map {
      case id~name~address => Merchant(id, name, address)
    }
  }

  def allMerchants() = {
    DB.withConnection { implicit c =>
      SQL("select * from merchants").as(merchant *)
    }
  }

  def getMerchant(id: Long) = {
    DB.withConnection { implicit c =>
      SQL("select * from merchants where id = {id}").on(
        'id -> id
      ).as(merchant *)
    }
  }

  //We don't know if we may have the same merchant with multiple address
  //or multiple merchants with the same name.  Assuming the latter.
  def getMerchantId(name: String): Option[Long] = {
    try {
      Some(DB.withConnection { implicit c =>
        SQL("select id from merchants where upper(name) = upper({name})").on(
          'name -> name
        ).apply().head[Long]("id")
      })
    } catch {
      case nse: NoSuchElementException => None
    }
  }

  def addMerchant(name: String, address: String) {
    //Add the merchant only if another merchant with the same name doesn't exit
    getMerchantId(name) match {
      case Some(x) => {}
      case None => DB.withConnection { implicit c =>
        SQL("insert into merchants (name, address) values ({name}, {address})").on(
          'name -> name,
          'address -> address
        ).executeUpdate()
      }
    }
  }

  def deleteMerchant(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from merchants where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }
}