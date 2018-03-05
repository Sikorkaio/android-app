package io.sikorka.android.core.accounts


interface PassphraseValidator {
  @ValidationResult.Code
  fun validate(passphrase: String, passphraseConfirmation: String): Int
}