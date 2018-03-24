package io.sikorka.android.core.configuration.peers

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.test.assertFails

class PeerTest {

  @Test
  fun matchPeer() {
    val url = "enode://a8a31c68cb91c0aa5b641cde877dce7c9113d55fb15103a4da004de5a6145ed4" +
      "929d899a9ef4ce51d3f57fc3d4a2d78fa2b7e50463c6aa29a8799a600464c8fe@192.168.90.19:30303"

    val peerEntry = Peer.peerFromNode(url)

    val expectedNodeId = "a8a31c68cb91c0aa5b641cde877dce7c9113d55fb15103a4da004de5a6145ed4" +
      "929d899a9ef4ce51d3f57fc3d4a2d78fa2b7e50463c6aa29a8799a600464c8fe"

    peerEntry.run {
      assertThat(nodeId).isEqualTo(expectedNodeId)
      assertThat(nodeAddress).isEqualTo("192.168.90.19")
      assertThat(nodePort).isEqualTo(30303)

      assertThat(toString()).isEqualTo(url)
      val secondPeerEntry = Peer.peerFromNode(url)

      assertThat(this).isEqualTo(secondPeerEntry)
      assertThat(hashCode()).isEqualTo(secondPeerEntry.hashCode())
    }
  }

  @Test
  fun passWeirdNodishThingy() {
    assertFails { Peer.peerFromNode("enode://1234@0129:0000") }.run {
      assertThat(this).isInstanceOf(IllegalArgumentException::class.java)
      assertThat(this)
        .hasMessageThat()
        .isEqualTo("String doesn't match node url format enode://1234@0129:0000")
    }
  }
}