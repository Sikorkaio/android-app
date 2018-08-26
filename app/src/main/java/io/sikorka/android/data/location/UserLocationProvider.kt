package io.sikorka.android.data.location

import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class UserLocationProvider
@Inject constructor() : MutableLiveData<UserLocation>() {
  init {
    value = UserLocation.default()
  }
}