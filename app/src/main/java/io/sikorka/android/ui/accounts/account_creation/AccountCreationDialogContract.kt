package io.sikorka.android.ui.accounts.account_creation

import io.sikorka.android.core.accounts.ValidationResult
import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter

interface AccountCreationDialogView : BaseView {
  fun showError(@ValidationResult.Code code: Long)
  fun complete()
}

interface AccountCreationDialogPresenter : Presenter<AccountCreationDialogView> {

  fun createAccount(passphrase: String, passphraseConfirmation: String)
}

