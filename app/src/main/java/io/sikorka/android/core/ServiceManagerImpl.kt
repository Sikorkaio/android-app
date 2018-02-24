package io.sikorka.android.core

import android.app.Application
import android.content.Intent
import io.sikorka.android.SikorkaService
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import javax.inject.Inject

class ServiceManagerImpl
@Inject
constructor(
  private val application: Application
) : ServiceManager {
  override fun start() {
    val intent = Intent(application, SikorkaService::class.java)
    application.startService(intent)
  }

  override fun stop() {
    val intent = Intent(application, SikorkaService::class.java)
    application.stopService(intent)
  }

  override fun restart() {
    async(CommonPool) {
      stop()
      delay(3000)
      start()
    }
  }
}