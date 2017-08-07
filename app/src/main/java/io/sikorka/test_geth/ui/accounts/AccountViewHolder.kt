package io.sikorka.test_geth.ui.accounts

import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import io.sikorka.test_geth.R
import io.sikorka.test_geth.mvp.BaseViewHolder
import io.sikorka.test_geth.ui.dialogs.showConfirmation
import org.ethereum.geth.Account

class AccountViewHolder(
    itemView: View,
    private val presenter: AccountAdapterPresenter
) : BaseViewHolder<Account>(itemView) {
  @BindView(R.id.account_address) internal lateinit var accountAddress: TextView

  @OnClick(R.id.accounts__delete)
  internal fun onDelete() {
    itemView.context.showConfirmation(
        R.string.accounts__delete_account_dialog_title,
        R.string.account__delete_account_dialog_content
    ) {
      onDelete?.invoke(presenter.item(adapterPosition))
    }
  }

  @OnClick(R.id.accounts__export)
  internal fun onExport() {
    onExport?.invoke(presenter.item(adapterPosition))
  }

  var onDelete: ((Account) -> Unit)? = null
  var onExport: ((Account) -> Unit)? = null

  init {
    ButterKnife.bind(this, itemView)
  }

  override fun update() {
    this.accountAddress.text = presenter.hex(adapterPosition)
  }
}