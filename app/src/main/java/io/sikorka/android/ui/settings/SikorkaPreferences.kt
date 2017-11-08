package io.sikorka.android.ui.settings


import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import io.sikorka.android.BuildConfig
import io.sikorka.android.R
import io.sikorka.android.helpers.fail
import io.sikorka.android.io.StorageManager
import io.sikorka.android.io.bytes
import io.sikorka.android.node.configuration.Network
import io.sikorka.android.settings.AppPreferences
import toothpick.Scope
import toothpick.Toothpick
import javax.inject.Inject


class SikorkaPreferences : PreferenceFragmentCompat() {

  @Inject lateinit var appPreferences: AppPreferences
  @Inject lateinit var storageManager: StorageManager

  private lateinit var scope: Scope

  override fun onDestroy() {
    super.onDestroy()
    Toothpick.closeScope(this)
  }

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    val context = context ?: fail("context was null")

    scope = Toothpick.openScopes(context.applicationContext, this)
    Toothpick.inject(this, scope)
    addPreferencesFromResource(R.xml.sikorka_preferences)
    val version = "${BuildConfig.VERSION_NAME}-${BuildConfig.GIT_HASH}"
    findPreference(R.string.preferences__about_version_key).summary = version
    findPreference(R.string.preferences__selected_network_key).apply {
      summary = networkSummary()
    }
    findPreference(R.string.preferences__usage_key).apply {
      val size = bytes(storageManager.storageUsed())
      val summaryText = getString(R.string.preferences__usage_summary, size)
      summary = summaryText
    }
  }

  private fun networkSummary(): String {
    val selectedNetwork = appPreferences.selectedNetwork()
    val resId = when (selectedNetwork) {
      Network.MAIN_NET -> R.string.networks__mainnet
      Network.ROPSTEN -> R.string.networks__ropsten
      Network.RINKEBY -> R.string.networks__rinkeby
      else -> throw IllegalArgumentException("network value is not supported $selectedNetwork")
    }
    return getString(resId)
  }

  private fun findPreference(@StringRes resId: Int): Preference = findPreference(getString(resId))
}
