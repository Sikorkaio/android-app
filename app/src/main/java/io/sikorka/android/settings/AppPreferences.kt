package io.sikorka.android.settings

import io.sikorka.android.core.configuration.Network

interface AppPreferences {

  fun selectNetwork(@Network.Selection network: Long)

  @Network.Selection
  fun selectedNetwork(): Long

  fun selectedAccount(): String

  fun selectAccount(account: String)

  fun preferredGasPrice(): Long

  fun setPreferredGasPrice(gasPrice: Long)

  fun preferredGasLimit(): Long

  fun setPreferredGasLimit(gasLimit: Long)
}