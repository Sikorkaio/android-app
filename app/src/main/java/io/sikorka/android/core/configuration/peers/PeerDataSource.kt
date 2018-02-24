package io.sikorka.android.core.configuration.peers

import io.reactivex.Completable
import io.reactivex.Single
import io.sikorka.android.helpers.Lce
import java.io.File

interface PeerDataSource {

  fun peers(): Single<Lce<List<PeerEntry>>>

  fun savePeers(
    peers: List<PeerEntry>,
    merge: Boolean = false
  ): Completable

  fun loadPeersFromUrl(url: String, merge: Boolean = true): Completable

  fun loadPeersFromFile(file: File, merge: Boolean = true): Completable
}