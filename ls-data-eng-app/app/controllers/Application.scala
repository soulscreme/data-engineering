package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._

object Application extends Controller {

  def index = Action {
    val orders = Order.allOrders()
    Ok(views.html.index(orders, Purchaser.allPurchasers(), Merchant.allMerchants(),
      Item.allItems(),
      orders.map(order => order.item.price.multiply(java.math.BigDecimal.valueOf(order.quantity))).reduce((total, cur) => total.add(cur))))
  }

  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("orderfile").map { inputFile =>
      scala.io.Source.fromFile(inputFile.ref.file).getLines().foreach( line =>
        line match {
          case "purchaser name\titem description\titem price\tpurchase count\tmerchant address\tmerchant name" => { println("found headers") }
          case _ => lineToRecord(line)
        }
      )
    }
    Ok("File uploaded")
  }

  def lineToRecord(line: String) {
    val symbols = new java.text.DecimalFormatSymbols();
    symbols.setGroupingSeparator(',');
    symbols.setDecimalSeparator('.');

    val pattern = "#,##0.0#"

    val decimalParser = new java.text.DecimalFormat(pattern, symbols)
    decimalParser.setParseBigDecimal(true)

    val columnVals = line.split("\\t")

    Purchaser.addPurchaser(columnVals(0))

    val purchaserId = Purchaser.getPurchaserId(columnVals(0))

    purchaserId match {
      case Some(pId) => {
        Merchant.addMerchant(columnVals(5), columnVals(4))

        val merchantId = Merchant.getMerchantId(columnVals(5))

        merchantId match {
          case Some(mId) => {
            Item.addItem(columnVals(1), decimalParser.parse(columnVals(2)).asInstanceOf[java.math.BigDecimal], mId)

            val itemId = Item.getItemId(columnVals(1), decimalParser.parse(columnVals(2)).asInstanceOf[java.math.BigDecimal], mId)

            itemId match {
              case Some(iId) => Order.addOrder(pId, iId, columnVals(3).toLong)
            }
          }
        }
      }
    }
  }

}