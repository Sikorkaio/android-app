package io.sikorka.test_geth.ui.accounts.creation_dialog

import io.sikorka.test_geth.node.accounts.ValidationResult
import io.sikorka.test_geth.node.accounts.AccountRepository
import io.sikorka.test_geth.node.accounts.PassphraseValidator
import io.sikorka.test_geth.mvp.BasePresenter
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