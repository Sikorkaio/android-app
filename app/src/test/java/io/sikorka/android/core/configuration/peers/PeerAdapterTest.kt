package io.sikorka.android.core.configuration.peers

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PeerAdapterTest {
  private val peerAdapter = PeerAdapter()
  @Test
  fun peerAdapterConversions() {
    val url = "enode://6f8a80d14311c39f35f516fa664deaaaa13e85b2f7493f37f6144d86991ec012937307647" +
      "bd3b9a82abe2974e1407241d54947bbb39763a4cac9f77166ad92a0" +
      "@10.3.58.6:30303"
    val peerEntry = peerAdapter.fromJson(url)
    assertThat(peerEntry.nodeAddress).isEqualTo("10.3.58.6")
    assertThat(peerAdapter.toJson(peerEntry)).isEqualTo(url)
  }
}