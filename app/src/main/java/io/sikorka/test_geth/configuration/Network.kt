package io.sikorka.test_geth.configuration

import android.support.annotation.IntDef

object Network {

  const val MAIN_NET = 1L
  const val ROPSTEN = 2L
  const val RINKEBY = 3L

  @IntDef()
  @Retention(AnnotationRetention.SOURCE)
  annotation class Selection
}