package io.sikorka.android.node.accounts

import io.sikorka.android.BuildConfig
import io.sikorka.android.helpers.fail
import javax.inject.Inject

class PassphraseValidatorImpl
@Inject constructor() : PassphraseValidator {
  override fun validate(passphrase: String, passphraseConfirmation: String): Long = when {
    passphrase.isBlank() -> ValidationResult.EMPTY_PASSPHRASE
    passphrase.length < PASSWORD_MIN_LENGTH && !BuildConfig.DEBUG -> ValidationResult.PASSWORD_SHORT
    passphrase != passphraseConfirmation -> ValidationResult.CONFIRMATION_MISSMATCH
    passphrase == passphraseConfirmation -> ValidationResult.OK
    else -> fail("not supported branch")
  }

  companion object {
    private const val PASSWORD_MIN_LENGTH = 8
  }
}