package io.sikorka.android.settings

import android.content.SharedPreferences
import androidx.annotation.IntRange
import io.sikorka.android.core.configuration.Network
import javax.inject.Inject

class AppPreferencesImpl
@Inject
constructor(private val sharedPreferences: SharedPreferences) : AppPreferences {

  @Network.Selection
  override fun selectedNetwork(): Int {
    return sharedPreferences.getInt(SELECTED_NETWORK, Network.ROPSTEN)
  }

  override fun selectNetwork(@Network.Selection network: Int) {
    sharedPreferences.edit()
        .putInt(SELECTED_NETWORK, network)
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

  @IntRange(from = 0, to = 10)
  override fun preferredBalancePrecision(): Int {
    return sharedPreferences.getInt(BALANCE_PRECISION, DEFAULT_BALANCE_PRECISION)
  }

  override fun setPreferredBalancePrecision(@IntRange(from = 0, to = 10) digits: Int) {
    sharedPreferences.edit()
        .putInt(BALANCE_PRECISION, digits)
        .apply()
  }

  companion object {
    private const val DEFAULT_BALANCE_PRECISION = 3
    private const val BALANCE_PRECISION = "io.sikorka.android.preferences.BALANCE_PRECISION"
    const val SELECTED_NETWORK = "io.sikorka.android.preferences.NETWORK"
    const val SELECTED_ACCOUNT = "io.sikorka.android.preferences.ACCOUNT"
    const val PREFERRED_GAS_PRICE = "io.sikorka.android.preferences.GAS_PRICE"
    const val PREFERRED_GAS_LIMIT = "io.sikorka.android.preferences.GAS_LIMIT"
  }
}