package io.sikorka.android.ui.detector.select

import android.support.annotation.IntDef

object SupportedDetectors {
  const val BLUETOOTH = 1


  @IntDef(BLUETOOTH.toLong())
  @Retention(AnnotationRetention.SOURCE)
  annotation class Detector
}