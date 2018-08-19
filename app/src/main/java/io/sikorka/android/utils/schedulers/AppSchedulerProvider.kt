package io.sikorka.android.utils.schedulers

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Provider

class AppSchedulerProvider
@Inject
constructor() : Provider<AppSchedulers> {

  private var count = 0
  private val executor = Executors.newSingleThreadExecutor { Thread(it, "db-operations") }
  private val monitorExecutor = Executors.newSingleThreadExecutor { Thread(it, "monitor") }
  private val computation = Executors.newFixedThreadPool(2) {
    Thread(it, "computation-${++count}")
  }

  override fun get(): AppSchedulers {
    return AppSchedulers(
      io = Schedulers.io(),
      computation = Schedulers.from(computation),
      main = AndroidSchedulers.mainThread(),
      db = Schedulers.from(executor),
      monitor = Schedulers.from(monitorExecutor)
    )
  }
}