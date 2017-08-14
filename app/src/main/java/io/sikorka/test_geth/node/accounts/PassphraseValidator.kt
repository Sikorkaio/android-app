package io.sikorka.test_geth.node.accounts


interface PassphraseValidator {
  @ValidationResult.Code
  fun validate(passphrase: String, passphraseConfirmation: String): Long
}