package io.sikorka.android.ui.accounts

import io.sikorka.android.eth.Account
import io.sikorka.android.node.accounts.AccountsModel

interface AccountAdapterPresenter {
  fun setData(data: AccountsModel)
  fun size(): Int
  fun item(position: Int): Account
  fun hex(position: Int): String
  fun isDefault(position: Int): Boolean
}