package io.sikorka.android.di.providers

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import io.sikorka.android.core.configuration.peers.PeerAdapter
import javax.inject.Inject
import javax.inject.Provider

class MoshiProvider
@Inject constructor() : Provider<Moshi> {
  override fun get(): Moshi {
    return Moshi.Builder()
      .add(PeerAdapter())
      .add(KotlinJsonAdapterFactory())
      .build()
  }
}