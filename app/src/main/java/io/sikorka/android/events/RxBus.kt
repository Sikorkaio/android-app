package io.sikorka.android.events

import io.reactivex.disposables.Disposable

interface RxBus {
  fun <T> register(receiver: Any, eventClass: Class<T>, onNext: (T) -> Unit)

  fun <T> register(receiver: Any, eventClass: Class<T>, main: Boolean, onNext: (T) -> Unit)

  fun unregister(receiver: Any)

  fun <T> register(eventClass: Class<T>, main: Boolean, onNext: (T) -> Unit): Disposable

  fun post(event: Any)
}