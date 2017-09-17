package io.sikorka.android.node.accounts

data class AccountModel(val account: String, val ethBalance: Double = NO_BALANCE) {
  companion object {
    const val NO_BALANCE = -1.0
  }
}