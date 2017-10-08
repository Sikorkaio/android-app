package io.sikorka.android.ui.accounts.account_creation

import io.reactivex.Scheduler
import io.sikorka.android.di.qualifiers.IoScheduler
import io.sikorka.android.di.qualifiers.MainScheduler
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.accounts.AccountRepository
import io.sikorka.android.node.accounts.PassphraseValidator
import io.sikorka.android.node.accounts.ValidationResult
import timber.log.Timber
import javax.inject.Inject

class AccountCreationDialogPresenterImpl
@Inject constructor(
    private val accountRepository: AccountRepository,
    private val passphraseValidator: PassphraseValidator,
    @IoScheduler private val ioScheduler: Scheduler,
    @MainScheduler private val mainScheduler: Scheduler
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
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribe({
          Timber.v("account created")
          view.complete()
        }) {

        })


  }
}