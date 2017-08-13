package io.sikorka.test_geth.accounts


interface PassphraseValidator {
  @ValidationResult.Code
  fun validate(passphrase: String, passphraseConfirmation: String): Long
}