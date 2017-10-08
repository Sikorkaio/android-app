package io.sikorka.android.ui.accounts

import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.accounts.AccountRepository
import org.ethereum.geth.Account
import javax.inject.Inject

class AccountPresenterImpl
@Inject
constructor(
    private val accountRepository: AccountRepository
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

  override fun deleteAccount(account: Account, passphrase: String) {
    val view = attachedView()
    if (passphrase.isEmpty()) {
      view.showError("")
    }
    accountRepository.deleteAccount(account, passphrase)
    loadAccounts()
  }
}