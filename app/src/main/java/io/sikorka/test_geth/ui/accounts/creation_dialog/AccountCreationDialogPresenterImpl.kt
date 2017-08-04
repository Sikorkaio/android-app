package io.sikorka.test_geth.ui.accounts.creation_dialog

import io.sikorka.test_geth.accounts.AccountRepository
import io.sikorka.test_geth.mvp.BasePresenter
import javax.inject.Inject

class AccountCreationDialogPresenterImpl
@Inject constructor(private val accountRepository: AccountRepository)
  : AccountCreationDialogPresenter,
    BasePresenter<AccountCreationDialogView>() {

  override fun createAccount(passphrase: String, passphraseConfirmation: String) {
    val view = attachedView()
    val errorCode = when {
      passphrase.isBlank() -> AccountCreationCodes.EMPTY_PASSPHRASE
      passphrase != passphraseConfirmation ->  AccountCreationCodes.CONFIRMATION_MISSMATCH
      else -> AccountCreationCodes.OK
    }

    if (errorCode != AccountCreationCodes.OK) {
      view.showError(errorCode)
      return
    }

    accountRepository.createAccount(passphrase)
  }
}