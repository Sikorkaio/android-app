package io.sikorka.android.ui.accounts.account_creation

import io.sikorka.android.core.accounts.AccountRepository
import io.sikorka.android.core.accounts.PassphraseValidator
import io.sikorka.android.core.accounts.ValidationResult
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class AccountCreationDialogPresenterImpl
@Inject constructor(
    private val accountRepository: AccountRepository,
    private val passphraseValidator: PassphraseValidator,
    private val schedulerProvider: SchedulerProvider
) : AccountCreationDialogPresenter,
    BasePresenter<AccountCreationDialogView>() {

  override fun createAccount(passphrase: String, passphraseConfirmation: String) {
    val view = attachedView()

    val code = passphraseValidator.validate(passphrase, passphraseConfirmation)

    if (code != ValidationResult.OK) {
      view.showError(code)
      return
    }

    addDisposable(accountRepository.createAccount(passphrase)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.main())
        .subscribe({
          Timber.v("account created")
          view.complete()
        }) {

        })


  }
}