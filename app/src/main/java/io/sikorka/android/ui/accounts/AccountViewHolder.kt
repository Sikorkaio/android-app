package io.sikorka.android.ui.accounts

import android.support.v7.widget.PopupMenu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import io.sikorka.android.R
import io.sikorka.android.core.model.Account
import io.sikorka.android.mvp.BaseViewHolder
import io.sikorka.android.ui.dialogs.showConfirmation
import io.sikorka.android.ui.dialogs.showInfo
import io.sikorka.android.ui.gone
import io.sikorka.android.ui.show
import kotterknife.bindView

class AccountViewHolder(
  itemView: View,
  private val presenter: AccountAdapterPresenter
) : BaseViewHolder<Account>(itemView), PopupMenu.OnMenuItemClickListener {

  var onDelete: AccountAction? = null
  var onExport: AccountAction? = null
  var onSetDefault: AccountAction? = null

  private val accountAddress: TextView by bindView(R.id.account_address)
  private val defaultIndicator: ImageView by bindView(R.id.account_management__account_default)
  private val actionsButton: ImageButton by bindView(R.id.account_management__more_actions)

  private fun onDelete() {
    if (presenter.size() == 1) {
      itemView.context.showInfo(
        R.string.account__delete_account_dialog_title,
        R.string.account_management__delete_last_account_content
      )
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
    actionsButton.setOnClickListener { view ->
      PopupMenu(view.context, view).run {
        setOnMenuItemClickListener(this@AccountViewHolder)
        inflate(R.menu.account_management__more_actions)
        show()
      }
    }
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