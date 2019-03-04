package com.gree.sync

import com.google.inject.Guice
import com.gree.sync.module.BaseModule
import com.gree.sync.schedule.FetchSchedule

object SyncApplication extends App {
  val injector = Guice.createInjector(new BaseModule())
  val schedule = injector.getInstance(classOf[FetchSchedule])
  schedule.run()
}
