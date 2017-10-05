package io.sikorka.android.ui.wizard

import io.reactivex.Scheduler
import io.sikorka.android.di.qualifiers.IoScheduler
import io.sikorka.android.di.qualifiers.MainScheduler
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.accounts.AccountRepository
import javax.inject.Inject

class WizardPresenterImpl
@Inject
constructor(
    private val accountRepository: AccountRepository,
    @IoScheduler private val ioScheduler: Scheduler,
    @MainScheduler private val mainScheduler: Scheduler
) : WizardPresenter, BasePresenter<WizardView>() {
  override fun checkForDefaultAccount() {
    addDisposable(accountRepository.accountsExist()
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .onErrorReturn { false }
        .subscribe({
          attachedView().accountsExists(it)
        }) {

        }
    )

  }
}