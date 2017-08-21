package io.sikorka.android.ui.accounts.account_creation

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import io.sikorka.android.node.accounts.ValidationResult

interface AccountCreationDialogView : BaseView {
  fun showError(@ValidationResult.Code code: Long)
  fun complete()
  fun onDismiss(action: (() -> Unit)? = null)
}

interface AccountCreationDialogPresenter : Presenter<AccountCreationDialogView> {

  fun createAccount(passphrase: String, passphraseConfirmation: String)
}

