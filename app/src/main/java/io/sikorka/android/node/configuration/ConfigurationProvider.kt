package io.sikorka.android.node.configuration

interface ConfigurationProvider {
  fun getActive(): IConfiguration
}