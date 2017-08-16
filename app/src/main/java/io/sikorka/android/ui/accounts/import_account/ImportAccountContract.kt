package io.sikorka.android.ui.accounts.import_account

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter

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
