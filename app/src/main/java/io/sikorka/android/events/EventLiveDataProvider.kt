package io.sikorka.android.events

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class EventLiveDataProvider {
  private val liveData = MutableLiveData<Event<Any>>()

  fun post(event: Event<Any>) {
    liveData.postValue(event)
  }

  fun observe(owner: LifecycleOwner, observer: (Event<Any>) -> Unit) {
    liveData.observe(owner, Observer { event: Event<Any>? ->
      if (event != null) {
        observer(event)
      }
    })
  }
}