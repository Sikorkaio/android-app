package io.sikorka.android.ui.accounts

import io.sikorka.android.node.accounts.AccountsModel
import org.ethereum.geth.Account

interface AccountAdapterPresenter {
  fun setData(data: AccountsModel)
  fun size(): Int
  fun item(position: Int): Account
  fun hex(position: Int): String
  fun isDefault(position: Int): Boolean
}