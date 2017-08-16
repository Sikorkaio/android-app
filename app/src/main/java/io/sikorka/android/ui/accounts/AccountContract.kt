package io.sikorka.android.ui.accounts

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import org.ethereum.geth.Account

interface AccountPresenter : Presenter<AccountView> {
  /**
   * Loads the accounts stored on the local keystore
   */
  fun loadAccounts()

  fun deleteAccount(account: Account)

}

interface AccountView : BaseView {
  fun accountsLoaded(accounts: List<Account>)
  fun loading()
  fun showError(message: String)
}