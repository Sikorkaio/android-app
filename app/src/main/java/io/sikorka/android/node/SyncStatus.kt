package io.sikorka.android.node

data class SyncStatus(val peers: Int, val currentBlock: Long, val highestBlock: Long)