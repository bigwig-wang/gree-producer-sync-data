package com.gree

import java.util.Properties

import com.gree.HttpClientUtils.get_with_params
import com.gree.KafkaProducer.KafkaProducerConfigs
import com.gree.ObjectFormatUtils.getRecord
import com.gree.TimeUtils.getBetweenDates

object SyncGreeApiData {
  case class apiConfigs() {
    val in = KafkaProducerConfigs.getClass.getClassLoader.getResourceAsStream("gree/api.properties")
    val properties = new Properties()
    properties.load(in)

    val url: String = properties.getProperty("gree.api.url")
    val startDateTime: String = properties.getProperty("gree.api.startTime")
    val endDateTime: String = properties.getProperty("gree.api.endTime")
  }

  def get_api_data_by_params(url: String, computer: Integer, startDateTime: String, endDateTime: String
                             , skipCount: Integer, maxResultCount: Integer): Unit = {
    //当前只取珠海基地的数据，code为4
    val data = get_with_params(url, computer, startDateTime, endDateTime, skipCount, maxResultCount)
    if (getRecord(data).nonEmpty) {
      KafkaProducer.produce(data)

      //TODO::偏移量存在redis里面，防止出现变量共享的问题，需要集成redis 历史数据和新数据key 记得区分一下
      val offset = skipCount + 1000
      get_api_data_by_params(url, computer, startDateTime, endDateTime, offset, maxResultCount)
    }
  }

  def sync_gree_new_data(): Unit = {
    //TODO::传入的时间是取首不取尾  关于历史数据修改了之后应该怎么处理
    val computer: Integer = 4
    val skipCount: Integer = 0
    val maxResultCount: Integer = 1000
    get_api_data_by_params(apiConfigs().url, computer, "11", "12", skipCount, maxResultCount)
  }

  def sync_gree_history_data(): Unit = {
    //TODO:: 当前只取了珠海基地数据，后面需要接入所有基地
    val computer: Integer = 4
    val skipCount: Integer = 0
    val maxResultCount: Integer = 1000

    val timeList = getBetweenDates(apiConfigs().startDateTime, apiConfigs().endDateTime)
    for (i <- 0 until timeList.size - 2) {
      val startTime = timeList.apply(i)
      val endTime = timeList.apply(i+1)
      get_api_data_by_params(apiConfigs().url, computer, startTime, endTime, skipCount, maxResultCount)
    }
  }
}
