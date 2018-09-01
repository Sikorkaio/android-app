package io.sikorka.android.ui.accounts.accountcreation

import io.sikorka.android.core.accounts.AccountRepository
import io.sikorka.android.core.accounts.PassphraseValidator
import io.sikorka.android.core.accounts.ValidationResult
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.utils.schedulers.AppSchedulers
import timber.log.Timber

class AccountCreationDialogPresenterImpl(
  private val accountRepository: AccountRepository,
  private val passphraseValidator: PassphraseValidator,
  private val appSchedulers: AppSchedulers
) : AccountCreationDialogPresenter, BasePresenter<AccountCreationDialogView>() {

  override fun createAccount(passphrase: String, passphraseConfirmation: String) {
    val view = attachedView()

    val code = passphraseValidator.validate(passphrase, passphraseConfirmation)

    if (code != ValidationResult.OK) {
      view.showError(code)
      return
    }

    addDisposable(accountRepository.createAccount(passphrase)
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.main)
      .subscribe({
        Timber.v("account created")
        view.complete()
      }) {
      })
  }
}