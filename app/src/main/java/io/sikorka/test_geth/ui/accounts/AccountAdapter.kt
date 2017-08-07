package io.sikorka.test_geth.ui.accounts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.sikorka.test_geth.R
import io.sikorka.test_geth.helpers.fail
import org.ethereum.geth.Account
import javax.inject.Inject

class AccountAdapter
@Inject
constructor(
    private val accountAdapterPresenter: AccountAdapterPresenter
) : RecyclerView.Adapter<AccountViewHolder>() {

  override fun onBindViewHolder(holder: AccountViewHolder?, position: Int) {
    holder?.update()
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AccountViewHolder {
    val inflater = LayoutInflater.from(parent?.context ?: fail("null context"))
    val view = inflater.inflate(R.layout.item_account, parent, false)
    val holder = AccountViewHolder(view, accountAdapterPresenter)
    return holder
  }

  override fun getItemCount(): Int = accountAdapterPresenter.size()

  fun update(accounts: List<Account>) {
    accountAdapterPresenter.setData(accounts)
    notifyDataSetChanged()
  }
}
