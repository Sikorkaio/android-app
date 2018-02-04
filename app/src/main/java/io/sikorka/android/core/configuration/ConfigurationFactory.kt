package io.sikorka.android.core.configuration

import android.app.Application
import toothpick.Toothpick
import javax.inject.Inject

class ConfigurationFactory
@Inject constructor(application: Application) {
  private val scope = Toothpick.openScope(application)

  fun configuration(@Network.Selection network: Int): IConfiguration {
    return when (network) {
      Network.ROPSTEN -> scope.getInstance(RopstenConfiguration::class.java)
      else -> error("Invalid selection")
    }
  }
}