package io.sikorka.android.ui.settings

interface DebugPreferencesStore {
  fun isLocationRandomizationEnabled(): Boolean
  fun setLocationRandomiztion(enabled: Boolean)
}