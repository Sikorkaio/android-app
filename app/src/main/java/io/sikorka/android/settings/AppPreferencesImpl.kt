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

  companion object {
    const val SELECTED_NETWORK = "io.sikorka.android.preferences.NETWORK"
    const val SELECTED_ACCOUNT = "io.sikorka.android.preferences.ACCOUNT"
  }
}