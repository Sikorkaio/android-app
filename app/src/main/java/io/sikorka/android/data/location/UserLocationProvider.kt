package io.sikorka.android.data.location

import android.arch.lifecycle.MutableLiveData
import javax.inject.Inject

class UserLocationProvider
@Inject constructor() : MutableLiveData<UserLocation>() {
  init {
    value = UserLocation.default()
  }
}