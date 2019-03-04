package com.gree.sync.schedule

import java.text.SimpleDateFormat
import java.util.Date

import com.google.inject.Inject
import com.google.inject.name.Named
import com.gree.sync.producer.KafkaProducer
import org.slf4j.LoggerFactory

class FetchSchedule @Inject()(producer: KafkaProducer) {

  val logger = LoggerFactory.getLogger("FetchSchedule")

  @Inject
  @Named("gree.basecode")
  var baseCodes: String = _

  def run(): Unit = {
    val date = getNowDate
    baseCodes.split(",").foreach(code =>
      this.run(date, code)
    )
  }

  def run(date: String): Unit = {
    baseCodes.split(",").foreach(code =>
      this.run(date, code)
    )
  }

  private[this] def run(date: String, computer: String): Unit = {
    logger.info("start send msg date is {} ", date)
    logger.info("start send msg computer is {}", computer)
    producer.doSend(date, computer)
  }

  def getNowDate(): String = {
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    dateFormat.format(now)
  }
}
