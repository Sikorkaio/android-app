package io.sikorka.android.utils.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SchedulerProviderImpl
@Inject constructor() : SchedulerProvider {
  override fun io(): Scheduler = Schedulers.io()

  override fun computation(): Scheduler = Schedulers.computation()

  override fun main(): Scheduler = AndroidSchedulers.mainThread()
}