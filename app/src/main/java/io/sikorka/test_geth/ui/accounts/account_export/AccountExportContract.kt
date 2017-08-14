package io.sikorka.test_geth.ui.accounts.account_export

import io.sikorka.test_geth.mvp.BaseView
import io.sikorka.test_geth.mvp.Presenter

interface AccountExportView : BaseView {
  fun showError(code: Long)
  fun exportComplete()

}

interface AccountExportPresenter : Presenter<AccountExportView> {
  fun export(accountHex: String, passphrase: String, encryptionPass: String, confirmation: String, path: String)
}