package io.sikorka.android.data.syncstatus

import androidx.lifecycle.MutableLiveData

class SyncStatusProvider : MutableLiveData<SyncStatus>() {
  init {
    postValue(SyncStatus())
  }
}