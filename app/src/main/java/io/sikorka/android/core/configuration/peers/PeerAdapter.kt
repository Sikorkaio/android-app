package io.sikorka.android.core.configuration.peers

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class PeerAdapter {

  @ToJson
  fun toJson(peerEntry: PeerEntry): String {
    TODO("not implemented")
  }

  @FromJson
  fun fromJson(peerEntry: String): PeerEntry {
    TODO("not implemented")
  }
}