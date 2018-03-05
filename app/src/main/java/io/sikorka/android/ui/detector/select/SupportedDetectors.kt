package io.sikorka.android.ui.detector.select

import android.support.annotation.IntDef

object SupportedDetectors {
  const val BLUETOOTH = 1
  const val MANUAL = 3
  const val QR_CODE = 2

  @IntDef(BLUETOOTH, MANUAL, QR_CODE)
  @Retention(AnnotationRetention.SOURCE)
  annotation class Detector
}