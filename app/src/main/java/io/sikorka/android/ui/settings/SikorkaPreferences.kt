package io.sikorka.android.ui.settings


import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import io.sikorka.android.BuildConfig
import io.sikorka.android.R


class SikorkaPreferences : PreferenceFragmentCompat() {

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.sikorka_preferences)

    val version = "${BuildConfig.VERSION_NAME}-${BuildConfig.GIT_HASH}"
    findPreference(R.string.preferences__about_version_key).summary = version
  }


  private fun findPreference(@StringRes resId: Int): Preference = findPreference(getString(resId))
}
