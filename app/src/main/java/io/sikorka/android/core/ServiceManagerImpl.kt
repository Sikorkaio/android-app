package io.sikorka.android.core

import android.content.Context
import android.content.Intent
import io.sikorka.android.SikorkaService
import io.sikorka.android.utils.schedulers.AppDispatchers
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class ServiceManagerImpl(
  private val context: Context,
  private val dispatchers: AppDispatchers
) : ServiceManager {
  override fun start() {
    val intent = Intent(context, SikorkaService::class.java)
    context.startService(intent)
  }

  override fun stop() {
    val intent = Intent(context, SikorkaService::class.java)
    context.stopService(intent)
  }

  override fun restart() {
    launch(dispatchers.io) {
      stop()
      delay(3000)
      start()
    }
  }
}