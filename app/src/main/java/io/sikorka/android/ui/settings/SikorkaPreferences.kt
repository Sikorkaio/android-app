package io.sikorka.android.ui.settings


import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import io.sikorka.android.BuildConfig
import io.sikorka.android.R
import io.sikorka.android.node.configuration.Network
import io.sikorka.android.settings.AppPreferences
import toothpick.Scope
import toothpick.Toothpick
import javax.inject.Inject


class SikorkaPreferences : PreferenceFragmentCompat() {

  @Inject lateinit var appPreferences: AppPreferences

  private lateinit var scope: Scope

  override fun onDestroy() {
    super.onDestroy()
    Toothpick.closeScope(this)
  }

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    scope = Toothpick.openScopes(context.applicationContext, this)
    Toothpick.inject(this, scope)
    addPreferencesFromResource(R.xml.sikorka_preferences)
    val version = "${BuildConfig.VERSION_NAME}-${BuildConfig.GIT_HASH}"
    findPreference(R.string.preferences__about_version_key).summary = version
    findPreference(R.string.preferences__selected_network_key).summary = networkSummary()
  }

  private fun networkSummary(): String {
    val selectedNetwork = appPreferences.selectedNetwork()
    val resId = when (selectedNetwork) {
      Network.MAIN_NET -> R.string.networks__mainnet
      Network.ROPSTEN -> R.string.networks__ropsten
      Network.RINKEBY -> R.string.networks__rinkeby
      else -> throw IllegalArgumentException("network value is not supported $selectedNetwork")
    }
    return getString(resId) ?: ""
  }

  private fun findPreference(@StringRes resId: Int): Preference = findPreference(getString(resId))
}
