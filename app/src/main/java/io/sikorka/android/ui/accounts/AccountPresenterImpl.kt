package io.sikorka.android.ui.accounts

import io.sikorka.android.node.accounts.AccountRepository
import io.sikorka.android.mvp.BasePresenter
import org.ethereum.geth.Account
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

  override fun deleteAccount(account: Account) {

    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}