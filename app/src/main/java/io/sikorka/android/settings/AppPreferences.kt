package io.sikorka.android.settings

import android.support.annotation.IntRange
import io.sikorka.android.core.configuration.Network

interface AppPreferences {

  fun selectNetwork(@Network.Selection network: Int)

  @Network.Selection
  fun selectedNetwork(): Int

  fun selectedAccount(): String

  fun selectAccount(account: String)

  fun preferredGasPrice(): Long

  fun setPreferredGasPrice(gasPrice: Long)

  fun preferredGasLimit(): Long

  fun setPreferredGasLimit(gasLimit: Long)

  @IntRange(from = 0, to = 10)
  fun preferredBalancePrecision(): Int

  fun setPreferredBalancePrecision(@IntRange(from = 0, to = 10) digits: Int)
}