package io.sikorka.android.core.ethereumclient

class LightClientProvider {

  private var _initialized: Boolean = false
  private var lightClient: LightClient? = null

  val initialized: Boolean
    get() = _initialized

  fun initialize(lightClient: LightClient) {
    this.lightClient = lightClient
    _initialized = true
  }

  fun get(): LightClient = checkNotNull(lightClient) { "light client was null" }

  fun reset() {
    _initialized = false
    lightClient = null
  }
}