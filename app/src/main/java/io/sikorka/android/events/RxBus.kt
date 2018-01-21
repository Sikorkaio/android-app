package io.sikorka.android.events

import io.reactivex.Observable
import kotlin.reflect.KClass

interface RxBus {
  fun <T> register(receiver: Any, eventClass: Class<T>, onNext: (T) -> Unit)

  fun <T : Any> observe(receiver: Any, eventClass: KClass<T>): Observable<T>

  fun unregister(receiver: Any)

  fun post(event: Any)
}