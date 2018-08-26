package io.sikorka.android.core.configuration.peers

import androidx.annotation.IntRange

data class PeerEntry(
  val nodeId: String,
  val nodeAddress: String,
  @IntRange(from = 1, to = 65535)
  val nodePort: Int
) {
  override fun toString(): String = "enode://$nodeId@$nodeAddress:$nodePort"
}