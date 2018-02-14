package io.sikorka.android.core.configuration.peers

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Completable
import io.reactivex.Single
import io.sikorka.android.core.configuration.ConfigurationProvider
import io.sikorka.android.core.configuration.Network
import io.sikorka.android.di.qualifiers.ApplicationCache
import io.sikorka.android.helpers.Lce
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Okio
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class PeerDataSourceImpl
@Inject constructor(
  private val configurationProvider: ConfigurationProvider,
  private val moshi: Moshi,
  @ApplicationCache
  private val cache: File
) : PeerDataSource {

  override fun peers(): Single<Lce<List<PeerEntry>>> = Single.fromCallable {
    val configuration = configurationProvider.getActive()
    require(configuration.network == Network.ROPSTEN) {
      "this is currently only supported for ROPSTEN"
    }

    val filePath = checkNotNull(configuration.peerFilePath) { "peer file path was null" }
    val peerFile = File(filePath)

    require(peerFile.exists()) { "couldn't find peer file" }

    val type = Types.newParameterizedType(List::class.java, PeerEntry::class.java)
    val adapter: JsonAdapter<List<PeerEntry>> = moshi.adapter(type)
    val peers = checkNotNull(adapter.fromJson(Okio.buffer(Okio.source(peerFile))))
    return@fromCallable Lce.success(peers)
  }.onErrorReturn { Lce.failure(it) }

  override fun savePeers(peers: List<PeerEntry>): Completable = Completable.fromCallable {
    val configuration = configurationProvider.getActive()
    require(configuration.network == Network.ROPSTEN) {
      "this is currently only supported for ROPSTEN"
    }

    val filePath = checkNotNull(configuration.peerFilePath) { "peer file path was null" }
    val peerFile = File(filePath)

    if (peerFile.exists()) {
      val delete = peerFile.delete()
      Timber.v("deleted the previous file ($delete)")
      val create = peerFile.createNewFile()
      Timber.v("new file created $create")

    }

    val type = Types.newParameterizedType(List::class.java, PeerEntry::class.java)
    val adapter: JsonAdapter<List<PeerEntry>> = moshi.adapter(type)

    Timber.v("saving ${peers.size} to ${peerFile.absolutePath}")
    Okio.buffer(Okio.sink(peerFile)).use {
      adapter.toJson(it, peers)
    }
  }

  override fun downloadPeers(
    url: String,
    replace: Boolean
  ): Completable = Completable.fromAction {
    val httpUrl = checkNotNull(HttpUrl.parse(url)) { "could not parse the url" }

    val client = OkHttpClient()
    val request = Request.Builder()
      .url(httpUrl)
      .build()

    val response = client.newCall(request).execute()
    if (!response.isSuccessful) {

    }

    val responseBody = response.body()
    val body = checkNotNull(responseBody) { "body was null" }
    require(body.contentLength() <= 512 * 1024) { "won't download files larger thant 512kB" }

    val temporary = File(cache, "peers.txt")
    if (temporary.exists()) {
      val delete = temporary.delete()
      Timber.v("deleted peer file ${temporary.absolutePath} - $delete")
    }

    Okio.buffer(body.source()).use {
      val bufferedSink = Okio.buffer(Okio.sink(temporary))
      bufferedSink.writeAll(it)
      bufferedSink.close()
    }

    val peers = Okio.buffer(Okio.source(temporary)).use {
      val peers: MutableList<PeerEntry> = ArrayList()

      while (it.exhausted()) {
        val line = checkNotNull(it.readUtf8Line()) { "null line" }
        val peer = try {
          Peer.getPeerInfo(line)
        } catch (ex: Exception) {
          line
        }

        val node = Peer.peerFromNode(peer)
        peers.add(node)
      }
      return@use peers
    }

    TODO("not implemented saving yet")
  }
}