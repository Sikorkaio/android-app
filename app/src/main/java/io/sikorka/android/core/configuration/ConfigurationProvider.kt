package io.sikorka.android.core.configuration

interface ConfigurationProvider {
  fun getActive(): IConfiguration
}