package io.sikorka.test_geth.ui.accounts

import io.sikorka.test_geth.mvp.BaseView
import io.sikorka.test_geth.mvp.Presenter
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