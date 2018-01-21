package io.sikorka.android.ui.accounts

import io.sikorka.android.core.accounts.AccountsModel
import io.sikorka.android.core.model.Account
import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter

interface AccountPresenter : Presenter<AccountView> {
  /**
   * Loads the accounts stored on the local keystore
   */
  fun loadAccounts()

  fun deleteAccount(account: Account, passphrase: String)
  fun setDefault(account: Account)

}

interface AccountView : BaseView {
  fun accountsLoaded(accounts: AccountsModel)
  fun loading()
  fun showError(message: String)
}