package io.sikorka.android.core.configuration.peers

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single
import io.sikorka.android.core.configuration.ConfigurationProvider
import io.sikorka.android.core.configuration.Network
import io.sikorka.android.helpers.Lce
import okio.Okio
import java.io.File
import javax.inject.Inject

class PeerDataSourceImpl
@Inject constructor(
  private val configurationProvider: ConfigurationProvider,
  private val moshi: Moshi
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
}