package io.sikorka.android.core.configuration.peers

import com.google.common.truth.Truth.assertThat
import io.sikorka.android.core.configuration.ConfigurationProvider
import io.sikorka.android.core.configuration.IConfiguration
import io.sikorka.android.core.configuration.Network.RINKEBY
import io.sikorka.android.core.configuration.Network.ROPSTEN
import io.sikorka.android.di.providers.MoshiProvider
import okio.Okio
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.File


class PeerDataSourceImplTest {

  private lateinit var peerDataSource: PeerDataSource

  @get:Rule
  var temporaryFolder = TemporaryFolder()

  @Mock
  private lateinit var configurationProvider: ConfigurationProvider
  @Mock
  private lateinit var configuration: IConfiguration

  private lateinit var file: File

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    val moshi = MoshiProvider().get()
    file = temporaryFolder.newFile("static-nodes.json")

    given(configurationProvider.getActive()).willReturn(configuration)
    peerDataSource = PeerDataSourceImpl(configurationProvider, moshi)
  }

  @After
  fun tearDown() {
  }

  @Test
  fun attemptToRetrievePeerListWithoutPeerFile() {
    given(configuration.network).willReturn(ROPSTEN)
    given(configuration.peerFilePath).willReturn("/mnt/path/somefile.json")

    peerDataSource.peers().test().run {
      awaitTerminalEvent()
      assertComplete()
      assertValueCount(1)
      val lce = values().first()
      assertThat(lce.failure()).isTrue()
      assertThat(lce.error()).run {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessageThat().isEqualTo("couldn't find peer file")
      }
    }
  }

  @Test
  fun attemptToRetrievePeerListOnOtherThanRopsten() {
    given(configuration.network).willReturn(RINKEBY)
    peerDataSource.peers().test().run {
      awaitTerminalEvent()
      assertComplete()
      assertValueCount(1)
      val lce = values().first()
      assertThat(lce.failure()).isTrue()
      assertThat(lce.error()).run {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessageThat().isEqualTo("this is currently only supported for ROPSTEN")
      }
    }
  }

  @Test
  fun attemptToRetrievePeerListNullPeerFilePath() {
    given(configuration.network).willReturn(ROPSTEN)
    given(configuration.peerFilePath).willReturn(null)

    peerDataSource.peers().test().run {
      awaitTerminalEvent()
      assertComplete()
      assertValueCount(1)
      val lce = values().first()
      assertThat(lce.failure()).isTrue()
      assertThat(lce.error()).run {
        isInstanceOf(IllegalStateException::class.java)
        hasMessageThat().isEqualTo("peer file path was null")
      }
    }
  }


  @Test
  fun attemptToRetrievePeerListWithoutAnError() {
    given(configuration.network).willReturn(ROPSTEN)
    val sink = Okio.buffer(Okio.sink(file))
    sink.writeUtf8(
      """
      [
        "enode://a8a31c68cb91c0aa5b641cde877dce7c9113d55fb15103a4da004de5a6145ed4929d899a9ef4ce51d3f57fc3d4a2d78fa2b7e50463c6aa29a8799a600464c8fe@139.162.250.93:30303",
        "enode://c144053a45b724332964174dae678557af8433f0f7fdc3dbd792a0def023fc15e95ae6e2a4b74277947bf1d5d2354ba58a0393f6aa600a7b82cbc127e29dc87d@192.168.90.17:30303"
      ]
    """.trimIndent()
    )
    sink.close()
    given(configuration.peerFilePath).willReturn(file.absolutePath)

    peerDataSource.peers().test().run {
      awaitTerminalEvent()
      assertComplete()
      assertValueCount(1)
      val response = values().first()

      assertThat(response.success()).isTrue()
      val data = response.data()

      assertThat(data).hasSize(2)
      assertThat(data[0].nodeAddress).isEqualTo("139.162.250.93")
      assertThat(data[1].nodeAddress).isEqualTo("192.168.90.17")
    }
  }

  @Test fun attemptToRetrievePeerListInvalidJson() {
    given(configuration.network).willReturn(ROPSTEN)
    val sink = Okio.buffer(Okio.sink(file))
    sink.writeUtf8(
      """
      {
        "key": 123
      }
    """.trimIndent()
    )
    sink.close()
    given(configuration.peerFilePath).willReturn(file.absolutePath)

    peerDataSource.peers().test().run {
      awaitTerminalEvent()
      assertComplete()
      assertValueCount(1)
      val response = values().first()

      assertThat(response.failure()).isTrue()
      assertThat(response.error()).run {
        isInstanceOf(com.squareup.moshi.JsonDataException::class.java)
        hasMessageThat().isEqualTo("Expected BEGIN_ARRAY but was BEGIN_OBJECT at path \$")
      }
    }
  }
}