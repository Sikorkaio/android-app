package io.sikorka.android.core.configuration.peers

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.test.assertFails

class PeerTest {

  @Test
  fun matchPeer() {
    val url = "enode://a8a31c68cb91c0aa5b641cde877dce7c9113d55fb15103a4da004de5a6145ed4929d899a9ef4ce51d3f57fc3d4a2d78fa2b7e50463c6aa29a8799a600464c8fe@192.168.90.19:30303"
    val peerEntry = Peer.peerFromNode(url)
    assertThat(peerEntry.nodeId).isEqualTo("a8a31c68cb91c0aa5b641cde877dce7c9113d55fb15103a4da004de5a6145ed4929d899a9ef4ce51d3f57fc3d4a2d78fa2b7e50463c6aa29a8799a600464c8fe")
    assertThat(peerEntry.nodeAddress).isEqualTo("192.168.90.19")
    assertThat(peerEntry.nodePort).isEqualTo(30303)

    assertThat(peerEntry.toString()).isEqualTo(url)
    val secondPeerEntry = Peer.peerFromNode(url)

    assertThat(peerEntry).isEqualTo(secondPeerEntry)
    assertThat(peerEntry.hashCode()).isEqualTo(secondPeerEntry.hashCode())
  }

  @Test
fun passWeirdNodishThingy() {
    val throwable = assertFails {
      Peer.peerFromNode("enode://1234@0129:0000")
    }

    assertThat(throwable).isInstanceOf(IllegalArgumentException::class.java)
    assertThat(throwable).hasMessageThat().isEqualTo("String doesn't match node url format enode://1234@0129:0000")
  }
}