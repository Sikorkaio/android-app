package io.sikorka.android.core.accounts

import io.sikorka.android.core.model.Account

data class AccountsModel(val defaultAddressHex: String, val accounts: List<Account>)