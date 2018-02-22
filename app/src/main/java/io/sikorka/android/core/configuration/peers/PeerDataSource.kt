package io.sikorka.android.core.configuration.peers

import io.reactivex.Completable
import io.reactivex.Single
import io.sikorka.android.helpers.Lce
import java.io.File

interface PeerDataSource {

  fun peers(): Single<Lce<List<PeerEntry>>>

  fun savePeers(
    peers: List<PeerEntry>,
    replace: Boolean = true
  ): Completable

  fun loadPeersFromUrl(url: String, replace: Boolean = false): Completable

  fun loadPeersFromFile(file: File, replace: Boolean = false): Completable
}