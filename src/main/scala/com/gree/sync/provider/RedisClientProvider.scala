package com.gree.sync.provider

import com.google.inject.name.Named
import com.google.inject.{Inject, Provider}
import com.gree.sync.utils.RedisClient
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.JedisPool

class RedisClientProvider extends Provider[RedisClient] {

  @Inject
  @Named("redis.port")
  var port: Int = _

  @Inject
  @Named("redis.host")
  var host: String = _

  @Inject
  @Named("redis.timeout")
  var timeout: Int = _

  @Inject
  @Named("redis.database")
  var database: Int = _

  val password: String = null

  def getJRedisPool(): JedisPool = {
    new JedisPool(new GenericObjectPoolConfig, host, port, timeout, password, database)
  }

  override def get(): RedisClient = {
    new RedisClient(getJRedisPool)
  }
}
