package io.sikorka.android.utils.schedulers

import kotlinx.coroutines.experimental.CoroutineDispatcher

data class AppDispatchers(
  val io: CoroutineDispatcher,
  val computation: CoroutineDispatcher,
  val main: CoroutineDispatcher,
  val db: CoroutineDispatcher,
  val monitor: CoroutineDispatcher
)