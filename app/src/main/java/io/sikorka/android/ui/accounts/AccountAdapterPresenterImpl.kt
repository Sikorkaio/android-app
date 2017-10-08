package io.sikorka.android.ui.accounts

import io.sikorka.android.node.accounts.AccountsModel
import org.ethereum.geth.Account
import javax.inject.Inject

class AccountAdapterPresenterImpl
@Inject constructor() : AccountAdapterPresenter {

  private var data: List<Account> = emptyList()
  private var default: String = ""

  override fun setData(data: AccountsModel) {
    this.data = data.accounts
    this.default = data.defaultAddressHex
  }

  override fun size(): Int = data.size

  override fun item(position: Int): Account = data[position]

  override fun hex(position: Int): String = item(position).address.hex

  override fun isDefault(position: Int): Boolean = item(position).address.hex == default
}