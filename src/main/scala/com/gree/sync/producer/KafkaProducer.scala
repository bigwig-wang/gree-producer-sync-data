package com.gree.sync.producer

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import com.google.inject.Inject
import com.gree.sync.utils.{ApiDataUtil, KafkaUtil, RedisClient}
import org.slf4j.LoggerFactory

class KafkaProducer @Inject()(api: ApiDataUtil, redis: RedisClient, kafka: KafkaUtil) {

  val logger = LoggerFactory.getLogger("KafkaProducer")

  val url: String = "http://sysapp.gree.com/GreeMesOpenApi/GreeMesApi/api/services/app/MesQCData/GetQCDatas"

  val topic: String = "test"

  def doSend(date: String, computer: String = "1") = {
    logger.debug(s"date is ${date},computer is ${computer}")
    updateOffset(date + computer, 0)
    var isFinish = false
    while (!isFinish) {
      isFinish = send2Kafka(date, computer, 1000)
    }
  }

  def send2Kafka(date: String, computer: String = "1", maxResult: Int = 1000): Boolean = {
    val offset: Int = getOffSet(date + computer)
    val resultJson = api.getData(
      url,
      computer,
      date,
      getTomorrowDate(date),
      offset,
      maxResult
    )
    val totalRecord: Int = resultJson.get("totalCount").getAsInt
    logger.debug("offset is {},totalRecord is {}", offset, totalRecord)
    val isNotFinish = totalRecord > offset
    if (isNotFinish) {
      val isOK = sendMessage(resultJson.toString)
      if (isOK) {
        if (totalRecord - offset < maxResult) {
          updateOffset(date + computer, totalRecord)
        } else {
          updateOffset(date + computer, offset + maxResult)
        }
      }
      logger.debug("isNotFinish is {}", false)

      false
    } else {
      logger.debug("no new records")
      true
    }
  }

  def getOffSet(key: String): Int = {
    var offset: Int = 0
    try {
      offset = new String(redis.get(key.getBytes)).toInt
    } catch {
      case e:Exception => logger.warn("no key {}",e)
    }
    offset
  }

  def updateOffset(key: String, i: Int) = {
    redis.set(key.getBytes(), i.toString.getBytes())
  }

  def sendMessage(msg: String) = {
//    logger.debug(msg)
    kafka.produce(topic, msg)
    logger.debug("finish send msg:{}",msg)
    true
  }

  def getTomorrowDate(dateStr: String): String = {
    val sdf = new SimpleDateFormat("yyyy-mm-dd")
    val date: Date = sdf.parse(dateStr)
    val cl = Calendar.getInstance()
    cl.setTime(date)
    cl.add(Calendar.DATE, 1)
    val tomorrow = sdf.format(cl.getTime())
    tomorrow
  }

}
