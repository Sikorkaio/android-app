package io.sikorka.android.di.providers

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import io.sikorka.android.core.configuration.peers.PeerAdapter
import io.sikorka.android.core.configuration.peers.PeerEntry
import javax.inject.Provider

class MoshiProvider : Provider<Moshi> {
  override fun get(): Moshi {
    return Moshi.Builder()
      .add(PeerAdapter())
      .add(KotlinJsonAdapterFactory())
      .build()
  }
}