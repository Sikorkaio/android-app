package io.sikorka.test_geth.ui.accounts.creation_dialog

import android.support.annotation.IntDef
import io.sikorka.test_geth.mvp.BaseView
import io.sikorka.test_geth.mvp.Presenter

interface AccountCreationDialogView : BaseView {
  fun showError(@AccountCreationCodes.ErrorCode code: Long)
  fun complete()
}

interface AccountCreationDialogPresenter : Presenter<AccountCreationDialogView> {

  fun createAccount(passphrase: String, passphraseConfirmation: String)
}

object AccountCreationCodes {
  const val EMPTY_PASSPHRASE = -1L
  const val CONFIRMATION_MISSMATCH = -2L
  const val OK = 0L

  @IntDef(EMPTY_PASSPHRASE, CONFIRMATION_MISSMATCH, OK)
  annotation class ErrorCode
}