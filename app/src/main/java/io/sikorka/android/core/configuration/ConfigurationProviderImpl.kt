package io.sikorka.android.core.configuration

import io.sikorka.android.settings.AppPreferences

class ConfigurationProviderImpl(
  private val configurationFactory: ConfigurationFactory,
  private val appPreferences: AppPreferences
) : ConfigurationProvider {
  override fun getActive(): IConfiguration {
    val configuration = configurationFactory.configuration(appPreferences.selectedNetwork())
    configuration.prepare()
    return configuration
  }
}