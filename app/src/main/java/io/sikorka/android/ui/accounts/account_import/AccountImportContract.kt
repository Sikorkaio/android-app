package io.sikorka.android.ui.accounts.account_import

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter

interface AccountImportView : BaseView {
  fun showError(code: Long)
  fun importSuccess()

}

interface AccountImportPresenter : Presenter<AccountImportView> {
  fun import(
      filePath: String,
      filePassphrase: String,
      accountPassphrase: String,
      accountPassphraseConfirmation: String
  )
}
