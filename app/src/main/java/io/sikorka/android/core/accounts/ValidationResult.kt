package io.sikorka.android.core.accounts

import android.support.annotation.IntDef

object ValidationResult {
  const val EMPTY_PASSPHRASE = -1
  const val CONFIRMATION_MISMATCH = -2
  const val PASSWORD_SHORT = -3
  const val OK = 0

  @IntDef(EMPTY_PASSPHRASE, CONFIRMATION_MISMATCH, OK, PASSWORD_SHORT)
  annotation class Code
}