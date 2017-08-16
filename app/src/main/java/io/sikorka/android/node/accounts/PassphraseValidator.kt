package io.sikorka.android.node.accounts


interface PassphraseValidator {
  @ValidationResult.Code
  fun validate(passphrase: String, passphraseConfirmation: String): Long
}