package com.gree

import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

object HttpClientUtils {

  def get(url: String): String = {
    val client: HttpClient = HttpClients.createDefault()

    val get = new HttpGet(url)
    val response = client.execute(get)
    val entity = response.getEntity
    EntityUtils.toString(entity, "UTF-8")
  }

  def get_with_params(url: String, computer: Integer, startTime: String,
                      endTime: String, skipCount: Integer, maxResult: Integer): String = {
    val sb = new StringBuilder(url)
    sb.append("?Computer=").append(computer).append("&StartDateTime=")
      .append(startTime).append("&EndDateTime=").append(endTime)
      .append("&SkipCount=").append(skipCount).append("&MaxResultCount=").append(maxResult)
    get(sb.toString())
  }
}
