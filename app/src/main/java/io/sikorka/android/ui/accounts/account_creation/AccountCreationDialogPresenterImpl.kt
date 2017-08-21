package io.sikorka.android.ui.accounts.account_creation

import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.accounts.AccountRepository
import io.sikorka.android.node.accounts.PassphraseValidator
import io.sikorka.android.node.accounts.ValidationResult
import javax.inject.Inject

class AccountCreationDialogPresenterImpl
@Inject constructor(
    private val accountRepository: AccountRepository,
    private val passphraseValidator: PassphraseValidator
)
  : AccountCreationDialogPresenter,
    BasePresenter<AccountCreationDialogView>() {

  override fun createAccount(passphrase: String, passphraseConfirmation: String) {
    val view = attachedView()

    val code = passphraseValidator.validate(passphrase, passphraseConfirmation)

    if (code != ValidationResult.OK) {
      view.showError(code)
      return
    }

    accountRepository.createAccount(passphrase)
  }
}