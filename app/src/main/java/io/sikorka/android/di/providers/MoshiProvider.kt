package io.sikorka.android.di.providers

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import javax.inject.Provider

class MoshiProvider : Provider<Moshi> {
  override fun get(): Moshi {
    return Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()
  }
}