package io.sikorka.android.node.accounts

import io.sikorka.android.eth.Account

data class AccountModel(
    val addressHex: String,
    val ethAccount: Account,
    val ethBalance: Double = NO_BALANCE
) {
  companion object {
    const val NO_BALANCE = -1.0
  }
}