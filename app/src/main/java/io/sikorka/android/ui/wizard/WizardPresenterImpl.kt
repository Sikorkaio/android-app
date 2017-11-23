package io.sikorka.android.ui.wizard

import io.sikorka.android.core.accounts.AccountRepository
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.utils.schedulers.SchedulerProvider
import javax.inject.Inject

class WizardPresenterImpl
@Inject
constructor(
    private val accountRepository: AccountRepository,
    private val schedulerProvider: SchedulerProvider
) : WizardPresenter, BasePresenter<WizardView>() {
  override fun checkForDefaultAccount() {
    addDisposable(accountRepository.accountsExist()
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.main())
        .onErrorReturn { false }
        .subscribe({
          attachedView().accountsExists(it)
        }) {

        }
    )

  }
}