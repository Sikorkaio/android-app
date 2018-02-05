package io.sikorka.android.core.configuration.peers


import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.test.assertFails

class PeerTest {

  @Test
  fun matchPeer() {
    val peerEntry =
      Peer.peerFromNode("enode://6f8a80d14311c39f35f516fa664deaaaa13e85b2f7493f37f6144d86991ec012937307647bd3b9a82abe2974e1407241d54947bbb39763a4cac9f77166ad92a0@10.3.58.6:30303")
    assertThat(peerEntry.nodeId).isEqualTo("6f8a80d14311c39f35f516fa664deaaaa13e85b2f7493f37f6144d86991ec012937307647bd3b9a82abe2974e1407241d54947bbb39763a4cac9f77166ad92a0")
    assertThat(peerEntry.nodeAddress).isEqualTo("10.3.58.6")
    assertThat(peerEntry.nodePort).isEqualTo(30303)
  }

  @Test
fun passWeirdNodishThingy() {
    val throwable = assertFails {
      Peer.peerFromNode("enode://1234@0129:0000")
    }

    assertThat(throwable).isInstanceOf(IllegalArgumentException::class.java)
    assertThat(throwable).hasMessageThat().isEqualTo("String doesn't match node url format")

  }


}