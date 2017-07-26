package io.sikorka.test_geth.ui.accounts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import io.sikorka.test_geth.R
import io.sikorka.test_geth.helpers.fail
import org.ethereum.geth.Account

class AccountAdapter : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {
  private var data: List<Account> = emptyList()

  override fun onBindViewHolder(holder: AccountViewHolder?, position: Int) {
    holder?.let {
      val adapterPosition = holder.adapterPosition
      val account = data[adapterPosition]
      holder.accountAddress.text = account.address.hex
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AccountViewHolder {
    val view = LayoutInflater.from(parent?.context ?: fail("null context"))
        .inflate(R.layout.item_account, parent, false)
    return AccountViewHolder(view)
  }

  override fun getItemCount(): Int = data.size

  fun update(accounts: List<Account>) {
    this.data = accounts
    notifyDataSetChanged()
  }


  class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.account_address) internal lateinit var accountAddress: TextView

    init {
      ButterKnife.bind(this, itemView)
    }
  }
}