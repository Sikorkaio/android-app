package io.sikorka.android.node.accounts

import io.sikorka.android.eth.Account

data class AccountsModel(val defaultAddressHex: String, val accounts: List<Account>)