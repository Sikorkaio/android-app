package io.sikorka.android.settings

import io.sikorka.android.node.configuration.Network

interface AppPreferences {

  fun selectNetwork(@Network.Selection network: Long)

  @Network.Selection
  fun selectedNetwork(): Long
}