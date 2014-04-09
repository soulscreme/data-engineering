package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.math.BigDecimal
import java.util.NoSuchElementException

case class Item(id: Long, description: String, price: BigDecimal, merchant: Merchant)

object Item {
  val item = {
    get[Long]("id") ~
      get[String]("description") ~
      get[BigDecimal]("price") ~
      get[Long]("merchant_id") map {
      case id~description~price~merchantId => Item(id, description, price, Merchant.getMerchant(merchantId)(0))
    }
  }

  def allItems() = {
    DB.withConnection { implicit c =>
      SQL("select * from items").as(item *)
    }
  }

  def getItem(id: Long) = {
    DB.withConnection { implicit c =>
      SQL("select * from items where id = {id}").on(
        'id -> id
      ).as(item *)
    }
  }

  def getItemId(description: String, price: java.math.BigDecimal, merchantId: Long): Option[Long] = {
    try{
      Some(DB.withConnection { implicit c =>
        SQL("select id from items where upper(description) = upper({description}) and price = {price}" +
          "and merchant_id = {merchantId}").on(
          'description -> description,
          'price -> price,
          'merchantId -> merchantId
        ).apply().head[Long]("id")
      })
    } catch {
      case nse: NoSuchElementException => None
    }
  }

  def addItem(description: String, price: java.math.BigDecimal, merchantId: Long) {
    //Add the item only if another item with the same description, price, and merchant does not exist
    getItemId(description, price, merchantId) match {
      case Some(x) => {}
      case None => DB.withConnection { implicit c =>
        SQL("insert into items (description, price, merchant_id) values ({description}, {price}, {merchant_id})").on(
          'description -> description,
          'price -> price,
          'merchant_id -> merchantId
        ).executeUpdate()
      }
    }
  }

  def deleteItem(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from items where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }
}