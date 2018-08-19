package io.sikorka.android.ui.wizard

import io.sikorka.android.core.accounts.AccountRepository
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.utils.schedulers.AppSchedulers
import javax.inject.Inject

class WizardPresenterImpl
@Inject
constructor(
  private val accountRepository: AccountRepository,
  private val appSchedulers: AppSchedulers
) : WizardPresenter, BasePresenter<WizardView>() {
  override fun checkForDefaultAccount() {
    addDisposable(accountRepository.accountsExist()
        .subscribeOn(appSchedulers.io)
        .observeOn(appSchedulers.main)
        .onErrorReturn { false }
        .subscribe({
          attachedView().accountsExists(it)
        }) {
        }
    )
  }
}