package io.sikorka.android.core.configuration.peers

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Completable
import io.reactivex.Single
import io.sikorka.android.core.configuration.ConfigurationProvider
import io.sikorka.android.core.configuration.Network
import io.sikorka.android.helpers.Lce
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Okio
import timber.log.Timber
import java.io.File

class PeerDataSourceImpl(
  private val configurationProvider: ConfigurationProvider,
  private val moshi: Moshi,
  private val cache: File
) : PeerDataSource {

  private fun loadFromFile(peerFile: File): List<PeerEntry> {
    check(peerFile.exists()) { "couldn't find peer file" }

    val type = Types.newParameterizedType(List::class.java, PeerEntry::class.java)
    val adapter: JsonAdapter<List<PeerEntry>> = moshi.adapter(type)
    return checkNotNull(adapter.fromJson(Okio.buffer(Okio.source(peerFile))))
  }

  private fun downloadPeers(url: String): File {
    val httpUrl = checkNotNull(HttpUrl.parse(url)) { "could not parse the url" }

    val client = OkHttpClient()
    val request = Request.Builder()
      .url(httpUrl)
      .build()

    val response = client.newCall(request).execute()
    if (!response.isSuccessful) {
      throw DownloadFailedException(response.code())
    }

    val responseBody = response.body()
    val body = checkNotNull(responseBody) { "body was null" }

    check(body.contentLength() <= 512 * 1024) { "won't download files larger thant 512kB" }

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
    return temporary
  }

  private fun parsePeerList(temporaryPeerFile: File): List<PeerEntry> {
    return Okio.buffer(Okio.source(temporaryPeerFile)).use {
      val peers: MutableList<PeerEntry> = ArrayList()

      while (!it.exhausted()) {
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
  }

  override fun peers(): Single<Lce<List<PeerEntry>>> = Single.fromCallable {
    val configuration = configurationProvider.getActive()
    check(configuration.network == Network.ROPSTEN) {
      "this is currently only supported for ROPSTEN"
    }

    val filePath = checkNotNull(configuration.peerFilePath) { "peer file path was null" }
    val peerFile = File(filePath)

    return@fromCallable Lce.success(loadFromFile(peerFile))
  }.onErrorReturn { Lce.failure(it) }

  override fun savePeers(
    peers: List<PeerEntry>,
    merge: Boolean
  ): Completable = Completable.fromCallable {
    val configuration = configurationProvider.getActive()
    check(configuration.network == Network.ROPSTEN) {
      "this is currently only supported for ROPSTEN"
    }

    val filePath = checkNotNull(configuration.peerFilePath) { "peer file path was null" }
    val peerFile = File(filePath)

    val type = Types.newParameterizedType(List::class.java, PeerEntry::class.java)
    val adapter: JsonAdapter<List<PeerEntry>> = moshi.adapter(type)

    val peersToSave = if (peerFile.exists() && !merge) {
      val delete = peerFile.delete()
      Timber.v("deleted the previous file ($delete)")
      val create = peerFile.createNewFile()
      Timber.v("new file created $create")
      peers
    } else {
      val existingPeers = try {
        loadFromFile(peerFile).toMutableList()
      } catch (ex: Exception) {
        Timber.v(ex)
        emptyList<PeerEntry>()
      }
      existingPeers.union(peers).toList()
    }

    Timber.v("saving ${peersToSave.size} to ${peerFile.absolutePath}")
    Okio.buffer(Okio.sink(peerFile)).use {
      adapter.toJson(it, peersToSave)
    }
  }

  override fun loadPeersFromFile(
    file: File,
    merge: Boolean
  ): Completable = Single.fromCallable {

    if (file.length() == 0L) {
      throw EmptyFileException()
    }

    return@fromCallable try {
      loadFromFile(file)
    } catch (ex: Exception) {
      Timber.v("Couldn't load peer list properly ${ex.message}")
      parsePeerList(file)
    }
  }.flatMapCompletable { peers -> savePeers(peers, merge) }

  override fun loadPeersFromUrl(
    url: String,
    merge: Boolean
  ): Completable = Single.fromCallable {
    downloadPeers(url)
  }.flatMapCompletable { loadPeersFromFile(it, merge) }
}