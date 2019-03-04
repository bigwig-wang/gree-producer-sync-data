package com.gree.sync.utils

import com.google.gson.{JsonObject, JsonParser}
import org.slf4j.LoggerFactory
import scalaj.http._

class ApiDataUtil() {

  val logger = LoggerFactory.getLogger("ApiDataUtil")

  def getData(url: String, computer: String, startTime: String,
              endTime: String, skipCount: Integer, maxResult: Integer): JsonObject = {
    logger.debug("get data from api:{},{},{}",url,computer,startTime)
    val json = new JsonParser()
    val result: HttpResponse[String] = Http(url)
      .param("Computer", computer.toString)
      .param("StartDateTime", startTime)
      .param("EndDateTime", endTime)
      .param("SkipCount", skipCount.toString)
      .param("MaxResultCount", maxResult.toString).asString
    if (result.isSuccess) {
      var resultJson = json.parse(result.body).asInstanceOf[JsonObject].getAsJsonObject("result")
      resultJson.addProperty("computer", computer.toString)
      resultJson
    } else {
      null
    }

  }

}