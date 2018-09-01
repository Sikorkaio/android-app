package io.sikorka.android.utils

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit

fun Disposable?.isDisposed(): Boolean = this?.isDisposed ?: true

fun <T> PublishRelay<T>.lastThrottle(
  duration: Long,
  unit: TimeUnit,
  action: (T) -> Unit,
  scheduler: Scheduler
): Disposable {
  return this.throttleLast(duration, unit, scheduler).subscribe({ action(it) }) { error ->
    Timber.e(error)
  }
}