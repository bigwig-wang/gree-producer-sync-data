package com.gree.utils

import com.gree.sync.utils.ApiDataUtil
import org.scalatest._

class UtilTest extends FunSuite with BeforeAndAfter {

  val url: String = "http://sysapp.gree.com/GreeMesOpenApi/GreeMesApi/api/services/app/MesQCData/GetQCDatas"

  before {
  }

  test("should get data access url") {
    val api = new ApiDataUtil()
    val response = api.getData(
      url,
      "1",
      "2019-01-01",
      "2019-01-01",
      1,
      100
    )
    assert(!response.toString.isEmpty)
  }

  after {

  }

}
