package io.sikorka.android.node.accounts

data class AccountModel(
    val addressHex: String,
    val ethBalance: Double = NO_BALANCE
) {
  companion object {
    const val NO_BALANCE = -1.0
  }
}