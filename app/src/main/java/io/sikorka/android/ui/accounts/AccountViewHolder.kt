package io.sikorka.android.ui.accounts

import android.support.v7.widget.PopupMenu
import android.view.MenuItem
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
import io.sikorka.android.ui.show
import org.ethereum.geth.Account

class AccountViewHolder(
    itemView: View,
    private val presenter: AccountAdapterPresenter
) : BaseViewHolder<Account>(itemView), PopupMenu.OnMenuItemClickListener {

  var onDelete: AccountAction? = null
  var onExport: AccountAction? = null
  var onSetDefault: AccountAction? = null

  @BindView(R.id.account_address)
  internal lateinit var accountAddress: TextView

  @BindView(R.id.account_management__account_default)
  lateinit var defaultIndicator: ImageView

  @OnClick(R.id.account_management__more_actions)
  internal fun onActionsPressed(view: View) {
    PopupMenu(view.context, view).run {
      setOnMenuItemClickListener(this@AccountViewHolder)
      inflate(R.menu.account_management__more_actions)
      show()
    }
  }

  private fun onDelete() {
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

  private fun onSetDefault() {
    if (presenter.isDefault(adapterPosition)) {

    } else {
      onSetDefault?.invoke(presenter.item(adapterPosition))
    }
  }

  override fun onMenuItemClick(item: MenuItem?): Boolean {

    when (item?.itemId) {
      R.id.account_management__action_default -> onSetDefault()
      R.id.account_management__action_delete -> onDelete()
      R.id.account_management__action_export -> onExport?.invoke(presenter.item(adapterPosition))
    }
    return true
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