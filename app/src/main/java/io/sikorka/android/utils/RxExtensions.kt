package io.sikorka.android.utils

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

fun Disposable?.isDisposed(): Boolean = this?.isDisposed ?: true

fun <T> PublishRelay<T>.lastThrottle(
  duration: Long,
  unit: TimeUnit,
  action: (T) -> Unit
): Disposable {
  return this.throttleLast(duration, unit).subscribe { action(it) }
}