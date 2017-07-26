package io.sikorka.test_geth.ui.accounts

import io.sikorka.test_geth.accounts.AccountRepository
import io.sikorka.test_geth.mvp.BasePresenter
import javax.inject.Inject

class AccountPresenterImpl
@Inject
constructor(
    private val accountRepository: AccountRepository
) : AccountPresenter, BasePresenter<AccountView>() {
  override fun loadAccounts() {
    addDisposable(accountRepository.accounts().subscribe({
      if (it.success()) {
        attachedView().accountsLoaded(it.data())
      } else if (it.loading()) {
        attachedView().loading()
      } else if (it.failure()) {
        attachedView().showError(it.error().message ?: "")
      }

    }))
  }
}