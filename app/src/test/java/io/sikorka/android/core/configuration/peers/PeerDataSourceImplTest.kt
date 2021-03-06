package io.sikorka.android.core.configuration.peers

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.squareup.moshi.Moshi
import io.sikorka.android.body
import io.sikorka.android.code
import io.sikorka.android.core.configuration.ConfigurationProvider
import io.sikorka.android.core.configuration.IConfiguration
import io.sikorka.android.core.configuration.Network.RINKEBY
import io.sikorka.android.core.configuration.Network.ROPSTEN
import io.sikorka.android.mockResponse
import io.sikorka.android.mockServer
import io.sikorka.android.success
import io.sikorka.android.url
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
import java.net.SocketTimeoutException
import java.nio.charset.Charset

class PeerDataSourceImplTest {

  private lateinit var peerDataSource: PeerDataSource

  @get:Rule
  var temporaryFolder = TemporaryFolder()

  @Mock
  private lateinit var configurationProvider: ConfigurationProvider
  @Mock
  private lateinit var configuration: IConfiguration

  private lateinit var file: File

  private fun file(name: String): String {
    val classLoader = checkNotNull(javaClass.classLoader)
    val resource = classLoader.getResource(name)

    return Okio.buffer(Okio.source(File(resource.path))).use {
      it.readString(Charset.forName("UTF-8"))
    }
  }

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    val moshi = Moshi.Builder()
      .add(PeerAdapter())
      .build()
    file = temporaryFolder.newFile("static-nodes.json")

    given(configurationProvider.getActive()).willReturn(configuration)
    peerDataSource = PeerDataSourceImpl(configurationProvider, moshi, temporaryFolder.newFolder())
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
        isInstanceOf(IllegalStateException::class.java)
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
        isInstanceOf(IllegalStateException::class.java)
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
    createMockStaticPeers(file("peers.json"))

    peerDataSource.peers().test().run {
      awaitTerminalEvent()
      assertComplete()
      assertValueCount(1)
      val response = values().first()

      assertThat(response.success()).isTrue()
      val data = response.data()

      assertThat(data).hasSize(2)
      assertThat(data[0].nodeAddress).isEqualTo("192.168.90.11")
      assertThat(data[1].nodeAddress).isEqualTo("192.168.90.12")
    }
  }

  @Test
  fun attemptToRetrievePeerListInvalidJson() {
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

  @Test
  fun attemptToPersistPeers() {
    given(configuration.network).willReturn(ROPSTEN)
    val firstNodeId = "a8a31c68cb91c0aa5b641cde877dce7c9113d55fb15103a4da004de5a6145ed4" +
      "929d899a9ef4ce51d3f57fc3d4a2d78fa2b7e50463c6aa29a8799a600464c8fe"
    val secondNodeId = "c144053a45b724332964174dae678557af8433f0f7fdc3dbd792a0def023fc15" +
      "e95ae6e2a4b74277947bf1d5d2354ba58a0393f6aa600a7b82cbc127e29dc87d"

    val list = listOf(
      PeerEntry(
        firstNodeId,
        "192.168.10.11",
        30303
      ),
      PeerEntry(
        secondNodeId,
        "192.168.90.17",
        30303
      )
    )

    given(configuration.peerFilePath).willReturn(file.absolutePath)

    val test = peerDataSource.savePeers(list).test()

    test.run {
      awaitTerminalEvent()
      assertComplete()
    }

    assertThat(file.exists()).isTrue()
    assertThat(file.length()).isGreaterThan(0)

    peerDataSource.peers().test().run {
      awaitTerminalEvent()
      assertComplete()
      assertValueCount(1)
      val response = values().first()

      assertThat(response.success()).isTrue()
      val data = response.data()

      assertThat(data).hasSize(2)
      assertThat(data[0].nodeAddress).isEqualTo("192.168.10.11")
      assertThat(data[1].nodeAddress).isEqualTo("192.168.90.17")
    }
  }

  @Test
  fun downloadPeerList() {
    given(configuration.network).willReturn(ROPSTEN)
    given(configuration.peerFilePath).willReturn(file.absolutePath)

    val mockWebServer = mockServer()

    mockWebServer.enqueue(mockResponse {
      success()
      body(file("peer_list.txt"))
    })

    val url = "http://${mockWebServer.hostName}:${mockWebServer.port}"

    peerDataSource.loadPeersFromUrl(url).test()
      .run {
        awaitTerminalEvent()
        assertComplete()
      }

    assertWithMessage("Peer file should now be populated")
      .that(file.length())
      .isGreaterThan(0)

    peerDataSource.peers().test().run {
      awaitTerminalEvent()
      assertComplete()
      assertValueCount(1)
      val response = values().first()

      assertWithMessage(response.errorMessage()).that(response.success()).isTrue()
      val data = response.data()

      assertThat(data).hasSize(2)

      assertThat(data[0].nodeAddress).isEqualTo("192.168.90.19")
      assertThat(data[1].nodeAddress).isEqualTo("192.168.90.17")
    }
  }

  @Test
  fun downloadPeerListNetworkError() {
    val mockServer = mockServer()
    peerDataSource.loadPeersFromUrl(mockServer.url()).test()
      .run {
        awaitTerminalEvent()
        assertError(SocketTimeoutException::class.java)
      }
  }

  @Test
  fun downloadPeerListInvalidResponse() {
    val mockServer = mockServer()
    mockServer.enqueue(mockResponse {
      success()
      body(
        """
        {"code":500}
      """.trimIndent()
      )
    })
    peerDataSource.loadPeersFromUrl(mockServer.url()).test()
      .run {
        awaitTerminalEvent()
        assertError(IllegalArgumentException::class.java)
        assertErrorMessage("String doesn't match node url format {\"code\":500}")
      }
  }

  @Test
  fun downloadPeerListHttpError() {
    val mockServer = mockServer()
    mockServer.enqueue(mockResponse {
      code(500)
    })
    peerDataSource.loadPeersFromUrl(mockServer.url()).test()
      .run {
        awaitTerminalEvent()
        assertError(DownloadFailedException::class.java)
        assertErrorMessage("download failed due to http code 500")
      }
  }

  @Test
  fun downloadPeerListMergePeers() {
    given(configuration.network).willReturn(ROPSTEN)
    createMockStaticPeers(file("peers2.json"))

    val mockWebServer = mockServer()

    mockWebServer.enqueue(mockResponse {
      success()
      body(file("peer_list.txt"))
    })

    peerDataSource.loadPeersFromUrl(mockWebServer.url(), true).test()
      .run {
        awaitTerminalEvent()
        assertComplete()
      }

    peerDataSource.peers().test().run {
      awaitTerminalEvent()
      assertComplete()
      assertValueCount(1)
      val response = values().first()

      assertThat(response.success())
      assertThat(response.data()).hasSize(4)
      assertThat(response.data().map { it.nodeAddress }).containsExactlyElementsIn(
        listOf(
          "192.168.90.19",
          "192.168.90.17",
          "192.168.10.11",
          "192.168.10.12"
        )
      )
    }
  }

  @Test
  fun downloadNodeListMergePeers() {
    given(configuration.network).willReturn(ROPSTEN)
    createMockStaticPeers(file("peers2.json"))

    val mockWebServer = mockServer()

    mockWebServer.enqueue(mockResponse {
      success()
      body(file("peers.json"))
    })

    peerDataSource.loadPeersFromUrl(mockWebServer.url(), true).test()
      .run {
        awaitTerminalEvent()
        assertComplete()
      }

    peerDataSource.peers().test().run {
      awaitTerminalEvent()
      assertComplete()
      assertValueCount(1)
      val response = values().first()

      assertThat(response.success())
      assertThat(response.data()).hasSize(4)
      assertThat(response.data().map { it.nodeAddress }).containsExactlyElementsIn(
        listOf(
          "192.168.10.11",
          "192.168.10.12",
          "192.168.90.11",
          "192.168.90.12"
        )
      )
    }
  }

  private fun createMockStaticPeers(data: String) {
    val sink = Okio.buffer(Okio.sink(file))
    sink.writeUtf8(data)
    sink.close()
    given(configuration.peerFilePath).willReturn(file.absolutePath)
  }
}