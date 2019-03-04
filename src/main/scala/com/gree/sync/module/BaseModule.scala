package com.gree.sync.module

import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Scopes}
import com.gree.sync.provider.RedisClientProvider
import com.gree.sync.utils.{PropertyUtil, RedisClient}

class BaseModule extends AbstractModule {

  override def configure(): Unit = {
    Names.bindProperties(binder(), PropertyUtil.loadFile("gree/kafka.properties"))
    Names.bindProperties(binder(), PropertyUtil.loadFile("redis.properties"))
    Names.bindProperties(binder(), PropertyUtil.loadFile("config.properties"))
    bind(classOf[RedisClient]).toProvider(classOf[RedisClientProvider]).in(Scopes.SINGLETON)
  }
}
