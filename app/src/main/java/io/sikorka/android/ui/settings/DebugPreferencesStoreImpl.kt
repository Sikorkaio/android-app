package io.sikorka.android.ui.settings

import android.content.SharedPreferences
import javax.inject.Inject

class DebugPreferencesStoreImpl
@Inject
constructor(private val sharedPreferences: SharedPreferences) : DebugPreferencesStore {
  override fun isLocationRandomizationEnabled(): Boolean {
    return sharedPreferences.getBoolean(LOCATION_RADNOMIZATION_SETTING_KEY, false)
  }

  override fun setLocationRandomiztion(enabled: Boolean) {
    sharedPreferences.edit().putBoolean(LOCATION_RADNOMIZATION_SETTING_KEY, enabled).apply()
  }

  companion object {
    private val LOCATION_RADNOMIZATION_SETTING_KEY = "pref_key_randomize_location"
  }
}