package io.sikorka.android.utils.schedulers

import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.rx2.asCoroutineDispatcher
import javax.inject.Inject
import javax.inject.Provider

class AppDispatcherProvider
@Inject
constructor(private val appSchedulers: AppSchedulers) : Provider<AppDispatchers> {
  override fun get(): AppDispatchers {
    return AppDispatchers(
      io = appSchedulers.io.asCoroutineDispatcher(),
      computation = appSchedulers.computation.asCoroutineDispatcher(),
      main = UI,
      db = appSchedulers.db.asCoroutineDispatcher(),
      monitor = appSchedulers.monitor.asCoroutineDispatcher()
    )
  }

}