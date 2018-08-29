package io.sikorka.android.core.configuration

import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class ConfigurationFactory : KoinComponent {

  fun configuration(@Network.Selection network: Int): IConfiguration {
    return when (network) {
      Network.ROPSTEN -> get<RopstenConfiguration>()
      else -> error("Invalid selection")
    }
  }
}