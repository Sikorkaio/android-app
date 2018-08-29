package io.sikorka.android.data.location

import androidx.lifecycle.MutableLiveData

class UserLocationProvider : MutableLiveData<UserLocation>() {
  init {
    value = UserLocation.default()
  }
}