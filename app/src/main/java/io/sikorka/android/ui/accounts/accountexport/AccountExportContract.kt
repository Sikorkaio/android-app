package io.sikorka.android.ui.accounts.accountexport

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter

interface AccountExportView : BaseView {
  fun showError(code: Int)
  fun exportComplete()
}

interface AccountExportPresenter : Presenter<AccountExportView> {
  fun export(
    accountHex: String,
    passphrase: String,
    encryptionPass: String,
    confirmation: String,
    path: String
  )
}