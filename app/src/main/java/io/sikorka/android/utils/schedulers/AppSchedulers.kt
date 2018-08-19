package io.sikorka.android.utils.schedulers

import io.reactivex.Scheduler

data class AppSchedulers(
  val io: Scheduler,
  val computation: Scheduler,
  val main: Scheduler,
  val db: Scheduler,
  val monitor: Scheduler
)