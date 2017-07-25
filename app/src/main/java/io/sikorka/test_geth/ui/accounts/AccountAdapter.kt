package io.sikorka.test_geth.ui.accounts

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import io.sikorka.test_geth.R

class AccountAdapter : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {
  override fun onBindViewHolder(holder: AccountViewHolder?, position: Int) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AccountViewHolder {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getItemCount(): Int {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }


  class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.account_address) internal lateinit var accountAddress: TextView

    init {
      ButterKnife.bind(this, itemView)
    }
  }
}