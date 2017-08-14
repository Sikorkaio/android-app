package io.sikorka.test_geth.node.accounts

import android.support.annotation.IntDef

object ValidationResult {
  const val EMPTY_PASSPHRASE = -1L
  const val CONFIRMATION_MISSMATCH = -2L
  const val PASSWORD_SHORT = -3L
  const val OK = 0L

  @IntDef(EMPTY_PASSPHRASE, CONFIRMATION_MISSMATCH, OK, PASSWORD_SHORT)
  annotation class Code
}