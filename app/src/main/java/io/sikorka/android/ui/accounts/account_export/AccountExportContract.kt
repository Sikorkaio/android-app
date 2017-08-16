package io.sikorka.android.ui.accounts.account_export

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter

interface AccountExportView : BaseView {
  fun showError(code: Long)
  fun exportComplete()

}

interface AccountExportPresenter : Presenter<AccountExportView> {
  fun export(accountHex: String, passphrase: String, encryptionPass: String, confirmation: String, path: String)
}