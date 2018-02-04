package io.sikorka.android.core.configuration

import android.support.annotation.IntDef

object Network {

  const val MAIN_NET = 1
  const val ROPSTEN = 2
  const val RINKEBY = 3

  @IntDef(
    MAIN_NET.toLong(),
    ROPSTEN.toLong(),
    RINKEBY.toLong()
  )
  @Retention(AnnotationRetention.SOURCE)
  annotation class Selection
}