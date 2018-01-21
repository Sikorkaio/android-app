package io.sikorka.android.ui.accounts

import io.sikorka.android.core.accounts.AccountsModel
import io.sikorka.android.core.model.Account

interface AccountAdapterPresenter {
  fun setData(data: AccountsModel)
  fun size(): Int
  fun item(position: Int): Account
  fun hex(position: Int): String
  fun isDefault(position: Int): Boolean
}