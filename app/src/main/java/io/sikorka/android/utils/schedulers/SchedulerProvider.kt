package io.sikorka.android.utils.schedulers

import io.reactivex.Scheduler

interface SchedulerProvider {

  fun io(): Scheduler

  fun computation(): Scheduler

  fun main(): Scheduler

}