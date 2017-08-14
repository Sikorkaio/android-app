package io.sikorka.test_geth.node.accounts

import javax.inject.Inject

class PassphraseValidatorImpl
@Inject constructor() : PassphraseValidator {
  override fun validate(passphrase: String, passphraseConfirmation: String): Long {
    return when {
      passphrase.isBlank() -> ValidationResult.EMPTY_PASSPHRASE
      passphrase != passphraseConfirmation -> ValidationResult.CONFIRMATION_MISSMATCH
      passphrase.length < PASSWORD_MIN_LENGTH -> ValidationResult.PASSWORD_SHORT
      else -> ValidationResult.OK
    }
  }

  companion object {
    private const val PASSWORD_MIN_LENGTH = 8
  }
}