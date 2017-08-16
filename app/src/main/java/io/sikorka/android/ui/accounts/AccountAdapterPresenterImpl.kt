package io.sikorka.android.ui.accounts

import org.ethereum.geth.Account
import javax.inject.Inject

class AccountAdapterPresenterImpl
@Inject constructor() : AccountAdapterPresenter {

  private var data: List<Account> = emptyList()

  override fun setData(data: List<Account>) {
    this.data = data
  }

  override fun size(): Int = data.size

  override fun item(position: Int): Account {
    return data[position]
  }

  override fun hex(position: Int): String = item(position).address.hex
}