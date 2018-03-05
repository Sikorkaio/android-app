package io.sikorka.android.ui.accounts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.sikorka.android.R
import io.sikorka.android.core.accounts.AccountsModel
import javax.inject.Inject

class AccountAdapter
@Inject
constructor(
  private val accountAdapterPresenter: AccountAdapterPresenter
) : RecyclerView.Adapter<AccountViewHolder>() {

  private var onDelete: AccountAction? = null
  private var onExport: AccountAction? = null
  private var onSetDefault: AccountAction? = null

  override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
    holder.let {
      it.onExport = onExport
      it.onDelete = onDelete
      it.onSetDefault = onSetDefault
      it.update()
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(R.layout.item__account, parent, false)
    return AccountViewHolder(view, accountAdapterPresenter)
  }

  override fun getItemCount(): Int = accountAdapterPresenter.size()

  fun update(accounts: AccountsModel) {
    accountAdapterPresenter.setData(accounts)
    notifyDataSetChanged()
  }

  fun setAccountActionListeners(
    onDelete: AccountAction? = null,
    onExport: AccountAction? = null,
    onSetDefault: AccountAction? = null
  ) {
    this.onDelete = onDelete
    this.onExport = onExport
    this.onSetDefault = onSetDefault
  }

}
