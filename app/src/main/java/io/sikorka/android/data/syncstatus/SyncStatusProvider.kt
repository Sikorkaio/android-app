package io.sikorka.android.data.syncstatus

import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class SyncStatusProvider
@Inject constructor() : MutableLiveData<SyncStatus>() {
  init {
    value = SyncStatus()
  }
}