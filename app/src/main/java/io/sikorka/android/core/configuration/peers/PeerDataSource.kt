package io.sikorka.android.core.configuration.peers

import io.reactivex.Single
import io.sikorka.android.helpers.Lce

interface PeerDataSource {
  fun peers(): Single<Lce<List<PeerEntry>>>
}