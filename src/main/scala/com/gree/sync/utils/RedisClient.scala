package com.gree.sync.utils

import org.slf4j.LoggerFactory
import redis.clients.jedis.Jedis
import redis.clients.util.Pool

class RedisClient(jedisPool: Pool[Jedis]) {

  val logger = LoggerFactory.getLogger("RedisUtil")

  def close(): Unit = {
    if (jedisPool != null)
      jedisPool.destroy()
  }

  def get(key: Array[Byte]): Array[Byte] = {
    val jedis = jedisPool.getResource
    val result: Array[Byte] = jedis.get(key)
    jedis.close()
    result
  }

  def set(key: Array[Byte], value: Array[Byte]): Unit = {
    try {
      val jedis = jedisPool.getResource
      jedis.set(key, value)
      jedis.close()
    } catch {
      case e: Exception => {
        logger.error(s"redis set error: ${e}")
      }
    }
  }
}
