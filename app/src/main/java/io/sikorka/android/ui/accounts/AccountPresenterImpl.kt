package io.sikorka.android.ui.accounts

import io.reactivex.Scheduler
import io.sikorka.android.di.qualifiers.IoScheduler
import io.sikorka.android.di.qualifiers.MainScheduler
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.accounts.AccountRepository
import org.ethereum.geth.Account
import timber.log.Timber
import javax.inject.Inject

class AccountPresenterImpl
@Inject
constructor(
    private val accountRepository: AccountRepository,
    @IoScheduler private val ioScheduler: Scheduler,
    @MainScheduler private val mainScheduler: Scheduler
) : AccountPresenter, BasePresenter<AccountView>() {
  override fun loadAccounts() {
    addDisposable(accountRepository.accounts().subscribe({
      val view = attachedView()
      when {
        it.success() -> view.accountsLoaded(it.data())
        it.loading() -> view.loading()
        it.failure() -> view.showError(it.error().message ?: "")
      }
    }))
  }

  override fun setDefault(account: Account) {
    addDisposable(accountRepository.setDefaultAccount(account)
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribe({
          loadAccounts()
        }) {

        })

  }

  override fun deleteAccount(account: Account, passphrase: String) {
    val view = attachedView()
    if (passphrase.isEmpty()) {
      view.showError("")
    }

    addDisposable(accountRepository.deleteAccount(account, passphrase)
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribe({
          loadAccounts()
        }) {
          view.showError("")
        })
  }
}