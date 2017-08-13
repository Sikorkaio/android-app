package io.sikorka.test_geth.accounts

import android.support.annotation.IntDef

object ValidationResult {
  const val EMPTY_PASSPHRASE = -1L
  const val CONFIRMATION_MISSMATCH = -2L
  const val OK = 0L

  @IntDef(EMPTY_PASSPHRASE, CONFIRMATION_MISSMATCH, OK)
  annotation class Code
}