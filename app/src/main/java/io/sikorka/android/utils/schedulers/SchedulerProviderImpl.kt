package io.sikorka.android.utils.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import javax.inject.Inject

class SchedulerProviderImpl
@Inject constructor() : SchedulerProvider {
  private val executor = Executors.newSingleThreadExecutor { Thread(it, "db-operations") }
  private val dbScheduler = Schedulers.from(executor)

  override fun db(): Scheduler = dbScheduler

  override fun io(): Scheduler = Schedulers.io()

  override fun computation(): Scheduler = Schedulers.computation()

  override fun main(): Scheduler = AndroidSchedulers.mainThread()
}