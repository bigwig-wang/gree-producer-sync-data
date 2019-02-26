package com.gree

import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.json4s.{CustomSerializer, DefaultFormats}
import org.json4s.JsonAST.{JDouble, JInt, JString}
import org.json4s.jackson.JsonMethods.parse

object ObjectFormatUtils {
  case class ApiInspection(categoryname: String,
                           partname: String,
                           partcode: String,
                           description: String,
                           enterprisename: String,
                           mlotno: String,
                           faillevel: String,
                           qcmodename: String,
                           statename: String,
                           lastcertified: String,
                           username: String,
                           executeddate: Timestamp,
                           purchasercode: String,
                           remarks: String,
                           qcquantity: Double,
                           failquantity: Double,
                           passquantity: Double,
                           failreason: String,
                           deliveryorderno: String,
                           receivedquantity: Double,
                           delivereddate: Timestamp,
                           finalcertified: String,
                           responsibleorganization: String,
                           sjremarks: String,
                           failreasoncode: String,
                           failcode: String
                          )

    class NumberSerializer extends CustomSerializer[Long](format => ( {
      case JString(x) => if (x.isEmpty) 0 else x.toLong
    }, {
      case x: Int => JInt(x)
    }
    ))

    class DoubleSerializer extends CustomSerializer[Double](format => ( {
      case JString(x) => if (x.isEmpty) 0 else x.toDouble
    }, {
      case x: Double => JDouble(x)
    }
    ))

    class TimeSerializer extends CustomSerializer[Timestamp](format => ( {
      case JInt(x) => new Timestamp(x.toLong)
    }, {
      case x: Timestamp => JInt(x.getTime)
    }
    ))

    def getRecord(record: String): List[ApiInspection] = {
      println("===================="+record+"======")
      implicit val formats = new DefaultFormats {
        override def dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
      } + new NumberSerializer() + new DoubleSerializer() + new TimeSerializer()
      val fi: List[ApiInspection] = (parse(record) \ "result" \ "items").extract[List[ApiInspection]]
      fi
    }
}
