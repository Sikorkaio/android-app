package io.sikorka.test_geth.ui.accounts.import_account

import io.sikorka.test_geth.mvp.BaseView
import io.sikorka.test_geth.mvp.Presenter

interface ImportAccountView : BaseView {
  fun showError(code: Long)
  fun importSuccess()

}

interface ImportAccountPresenter : Presenter<ImportAccountView> {
  fun import(
      filePath: String,
      filePassphrase: String,
      accountPassphrase: String,
      accountPassphraseConfirmation: String
  )
}
