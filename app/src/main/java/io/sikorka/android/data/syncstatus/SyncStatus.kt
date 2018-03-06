package io.sikorka.android.data.syncstatus

data class SyncStatus(
  val syncing: Boolean = false,
  val peers: Int = 0,
  val currentBlock: Long = 0,
  val highestBlock: Long = 0
)