package io.sikorka.test_geth.ui.accounts.creation_dialog

import io.sikorka.test_geth.node.accounts.ValidationResult
import io.sikorka.test_geth.mvp.BaseView
import io.sikorka.test_geth.mvp.Presenter

interface AccountCreationDialogView : BaseView {
  fun showError(@ValidationResult.Code code: Long)
  fun complete()
  fun onDismiss(action: (() -> Unit)? = null)
}

interface AccountCreationDialogPresenter : Presenter<AccountCreationDialogView> {

  fun createAccount(passphrase: String, passphraseConfirmation: String)
}

