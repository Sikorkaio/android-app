package io.sikorka.android.ui.accounts

import io.sikorka.android.core.accounts.AccountRepository
import io.sikorka.android.core.model.Account
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.utils.schedulers.AppSchedulers

class AccountPresenterImpl(
  private val accountRepository: AccountRepository,
  private val appSchedulers: AppSchedulers
) : AccountPresenter, BasePresenter<AccountView>() {
  override fun loadAccounts() {
    addDisposable(accountRepository.accounts().subscribe({
      val view = attachedView()
      when {
        it.success() -> view.accountsLoaded(it.data())
        it.loading() -> view.loading()
        it.failure() -> view.showError(it.error().message ?: "")
      }
    }) {})
  }

  override fun setDefault(account: Account) {
    addDisposable(accountRepository.setDefaultAccount(account)
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.main)
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
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.main)
      .subscribe({
        loadAccounts()
      }) {
        view.showError("")
      })
  }
}