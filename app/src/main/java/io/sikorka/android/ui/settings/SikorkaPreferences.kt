package io.sikorka.android.ui.settings


import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import io.sikorka.android.BuildConfig
import io.sikorka.android.R
import io.sikorka.android.core.configuration.Network
import io.sikorka.android.helpers.fail
import io.sikorka.android.io.StorageManager
import io.sikorka.android.io.bytes
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.ui.dialogs.balancePrecisionDialog
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

    with(findPreference(R.string.preferences__balance_precision_key)) {
      val digits = appPreferences.preferredBalancePrecision()
      summary = precisionSummary(digits)
      setOnPreferenceClickListener {

        val activity = activity ?: return@setOnPreferenceClickListener false

        val dialog = activity.balancePrecisionDialog { digits ->
          appPreferences.setPreferredBalancePrecision(digits)
          summary = precisionSummary(digits)
        }

        dialog.show()
        return@setOnPreferenceClickListener true
      }
    }

  }

  private fun precisionSummary(digits: Int) =
      resources.getQuantityString(R.plurals.preferences__balance_precision_summary, digits, digits)

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
