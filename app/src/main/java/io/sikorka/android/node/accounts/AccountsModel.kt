package io.sikorka.android.node.accounts

import org.ethereum.geth.Account

data class AccountsModel(val defaultAddressHex: String, val accounts: List<Account>)