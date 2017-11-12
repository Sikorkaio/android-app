package io.sikorka.android.node.ethereumclient

import javax.inject.Inject
import javax.inject.Provider

class LightClientProvider
@Inject
constructor() : Provider<LightClient> {

  private var _initialized: Boolean = false
  private lateinit var lightClient: LightClient

  val initialized: Boolean
    get() = _initialized

  fun initialize(lightClient: LightClient) {
    this.lightClient = lightClient
    _initialized = true
  }

  override fun get(): LightClient = lightClient
}