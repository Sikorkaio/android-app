package io.sikorka.android.events

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.HashMap
import java.util.LinkedList
import kotlin.reflect.KClass

class RxBusImpl : RxBus {
  private val serializedRelay = PublishRelay.create<Any>().toSerialized()
  private val activeSubscriptions = HashMap<Any, MutableList<Disposable>>()

  @Suppress("UNCHECKED_CAST")
  override fun <T> register(receiver: Any, eventClass: Class<T>, onNext: (T) -> Unit) {
    //noinspection unchecked
    val subscription = serializedRelay.filter {
      it.javaClass == eventClass
    }.map { obj -> obj as T }.subscribe(onNext)

    updateSubscriptions(receiver, subscription)
  }

  private fun updateSubscriptions(receiver: Any, subscription: Disposable) {
    val subscriptions: MutableList<Disposable> = activeSubscriptions[receiver] ?: LinkedList()
    subscriptions.add(subscription)
    activeSubscriptions[receiver] = subscriptions
  }

  override fun unregister(receiver: Any) {
    val subscriptions = activeSubscriptions.remove(receiver)
    if (subscriptions != null) {
      Observable.fromIterable(subscriptions).filter { !it.isDisposed }.subscribe { it.dispose() }
    }
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T : Any> observe(receiver: Any, eventClass: KClass<T>): Observable<T> {
    return serializedRelay.filter { it.javaClass == eventClass }.map { obj -> obj as T }
  }

  override fun post(event: Any) {
    serializedRelay.accept(event)
  }
}