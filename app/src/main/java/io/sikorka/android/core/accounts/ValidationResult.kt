package io.sikorka.android.core.accounts

import android.support.annotation.IntDef

object ValidationResult {
  const val EMPTY_PASSPHRASE = -1L
  const val CONFIRMATION_MISMATCH = -2L
  const val PASSWORD_SHORT = -3L
  const val OK = 0L

  @IntDef(EMPTY_PASSPHRASE, CONFIRMATION_MISMATCH, OK, PASSWORD_SHORT)
  annotation class Code
}