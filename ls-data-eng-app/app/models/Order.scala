package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Order(id: Long, customer: Purchaser, item: Item, quantity: Long)

object Order {
  val order = {
    get[Long]("id") ~
      get[Long]("purchaser_id") ~
      get[Long]("item_id") ~
      get[Long]("quantity") map {
      case id~purchaserId~itemId~quantity => Order(id, Purchaser.getPurchaser(purchaserId)(0), Item.getItem(itemId)(0), quantity)
    }
  }

  def allOrders() = {
    DB.withConnection { implicit c =>
      SQL("select * from orders").as(order *)
    }
  }

  def getOrder(id: Long) = {
    DB.withConnection { implicit c =>
      SQL("select * from orders where id = {id}").on(
        'id -> id
      ).as(order *)
    }
  }

  def addOrder(purchaserId: Long, itemId: Long, quantity: Long) {
    //Unlike others we will assume that every order uploaded is unique.
    //A customer might buy the same exact thing multiple times.
    DB.withConnection { implicit c =>
      SQL("insert into orders (purchaser_id, item_id, quantity) values ({purchaserId}, {itemId}, {quantity})").on(
        'purchaserId -> purchaserId,
        'itemId -> itemId,
        'quantity -> quantity
      ).executeUpdate()
    }
  }

  def deleteOrder(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from orders where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }
}