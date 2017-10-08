package io.sikorka.android.ui.accounts

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import io.sikorka.android.R
import io.sikorka.android.mvp.BaseViewHolder
import io.sikorka.android.ui.dialogs.showConfirmation
import io.sikorka.android.ui.dialogs.showInfo
import io.sikorka.android.ui.gone
import io.sikorka.android.ui.hide
import io.sikorka.android.ui.show
import org.ethereum.geth.Account

class AccountViewHolder(
    itemView: View,
    private val presenter: AccountAdapterPresenter
) : BaseViewHolder<Account>(itemView) {

  var onDelete: AccountAction? = null
  var onExport: AccountAction? = null

  @BindView(R.id.account_address)
  internal lateinit var accountAddress: TextView

  @BindView(R.id.account_management__account_default)
  lateinit var defaultIndicator: ImageView

  @OnClick(R.id.accounts__delete)
  internal fun onDelete() {
    if (presenter.size() == 1) {
      itemView.context.showInfo(R.string.account__delete_account_dialog_title,
          R.string.account_management__delete_last_account_content)
      return
    }
    itemView.context.showConfirmation(
        R.string.account__delete_account_dialog_title,
        R.string.account__delete_account_dialog_content
    ) {
      onDelete?.invoke(presenter.item(adapterPosition))
    }
  }

  @OnClick(R.id.accounts__export)
  internal fun onExport() {
    onExport?.invoke(presenter.item(adapterPosition))
  }

  init {
    ButterKnife.bind(this, itemView)
  }

  override fun update() {
    this.accountAddress.text = presenter.hex(adapterPosition)
    if (presenter.isDefault(adapterPosition)) {
      defaultIndicator.show()
    } else {
      defaultIndicator.gone()
    }
  }
}