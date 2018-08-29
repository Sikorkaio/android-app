package io.sikorka.android.data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) {
  this.observe(lifecycleOwner, Observer {
    if (it != null) {
      observer(it)
    }
  })
}