package io.sikorka.android.settings

import android.content.SharedPreferences
import io.sikorka.android.node.configuration.Network
import javax.inject.Inject

class AppPreferencesImpl
@Inject
constructor(private val sharedPreferences: SharedPreferences) : AppPreferences {

  @Network.Selection
  override fun selectedNetwork(): Long {
    return sharedPreferences.getLong(SELECTED_NETWORK, Network.ROPSTEN)
  }

  override fun selectNetwork(@Network.Selection network: Long) {
    sharedPreferences.edit()
        .putLong(SELECTED_NETWORK, network)
        .apply()
  }

  override fun selectedAccount(): String {
    return sharedPreferences.getString(SELECTED_ACCOUNT, "")
  }

  override fun selectAccount(account: String) {
    sharedPreferences.edit()
        .putString(SELECTED_ACCOUNT, account)
        .apply()
  }

  override fun preferredGasPrice(): Long {
    return sharedPreferences.getLong(PREFERRED_GAS_PRICE, -1)
  }

  override fun setPreferredGasPrice(gasPrice: Long) {
    sharedPreferences.edit()
        .putLong(PREFERRED_GAS_PRICE, gasPrice)
        .apply()
  }

  override fun preferredGasLimit(): Long {
    return sharedPreferences.getLong(PREFERRED_GAS_LIMIT, -1)
  }

  override fun setPreferredGasLimit(gasLimit: Long) {
    sharedPreferences.edit()
        .putLong(PREFERRED_GAS_LIMIT, gasLimit)
        .apply()
  }

  companion object {
    const val SELECTED_NETWORK = "io.sikorka.android.preferences.NETWORK"
    const val SELECTED_ACCOUNT = "io.sikorka.android.preferences.ACCOUNT"
    const val PREFERRED_GAS_PRICE = "io.sikorka.android.preferences.GAS_PRICE"
    const val PREFERRED_GAS_LIMIT = "io.sikorka.android.preferences.GAS_LIMIT"
  }
}