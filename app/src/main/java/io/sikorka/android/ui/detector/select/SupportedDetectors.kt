package io.sikorka.android.ui.detector.select

import android.support.annotation.IntDef

object SupportedDetectors {
  const val BLUETOOTH = 1
  const val MANUAL = 0

  @IntDef(BLUETOOTH.toLong(), MANUAL.toLong())
  @Retention(AnnotationRetention.SOURCE)
  annotation class Detector
}