package io.sikorka.android.core.configuration.peers

import java.util.regex.Pattern

object Peer {
  private val regex =
    "enode://([0-9a-fA-F]{128})@(((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)):([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])"
  private val nodeUrl = Pattern.compile(regex)

  fun peerFromNode(url: String): PeerEntry {
    val matcher = nodeUrl.matcher(url)
    require(matcher.matches()) { "String doesn't match node url format" }
    return PeerEntry(
      matcher.group(1),
      matcher.group(2),
      matcher.group(6).toInt()
    )
  }
}