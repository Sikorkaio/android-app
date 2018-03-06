package io.sikorka.android.ui.detector.select

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import io.sikorka.android.ui.detector.select.SupportedDetectors.Detector

data class SupportedDetector(
  @Detector val id: Int,
  @StringRes val title: Int,
  @DrawableRes val icon: Int
)