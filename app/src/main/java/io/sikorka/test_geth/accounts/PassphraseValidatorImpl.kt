package io.sikorka.test_geth.accounts

import javax.inject.Inject

class PassphraseValidatorImpl
@Inject constructor(): PassphraseValidator {
  override fun validate(passphrase: String, passphraseConfirmation: String): Long {
    return when {
      passphrase.isBlank() -> ValidationResult.EMPTY_PASSPHRASE
      passphrase != passphraseConfirmation ->  ValidationResult.CONFIRMATION_MISSMATCH
      else -> ValidationResult.OK
    }
  }
}