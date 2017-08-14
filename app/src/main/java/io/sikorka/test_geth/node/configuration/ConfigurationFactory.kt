package io.sikorka.test_geth.node.configuration

import android.app.Application
import toothpick.Toothpick
import javax.inject.Inject

class ConfigurationFactory
@Inject constructor(application: Application) {

  val scope = Toothpick.openScope(application)

  fun configuration(@Network.Selection network: Long): IConfiguration {
    return when (network) {
      Network.ROPSTEN -> scope.getInstance(RopstenConfiguration::class.java)
      else -> {
        throw IllegalArgumentException("Invalid selection")
      }
    }
  }
}