package io.sikorka.android.ui.settings

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.sikorka.android.BuildConfig
import io.sikorka.android.R
import io.sikorka.android.core.configuration.Network
import io.sikorka.android.core.configuration.Network.ROPSTEN
import io.sikorka.android.io.StorageManager
import io.sikorka.android.io.bytes
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.ui.dialogs.balancePrecisionDialog
import io.sikorka.android.ui.settings.peermanager.PeerManagerActivity
import org.koin.android.ext.android.inject

class SikorkaPreferences : PreferenceFragmentCompat() {

  private val appPreferences: AppPreferences by inject()

  private val storageManager: StorageManager by inject()

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    val context = requireContext()

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

        val dialog = activity.balancePrecisionDialog(digits) { digits ->
          appPreferences.setPreferredBalancePrecision(digits)
          summary = precisionSummary(digits)
        }

        dialog.show()
        return@setOnPreferenceClickListener true
      }
    }

    findPreference("pref_key_peer_manager").run {
      if (appPreferences.selectedNetwork() != ROPSTEN) {
        this.isVisible = false
      }
      setOnPreferenceClickListener {
        PeerManagerActivity.start(context)
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