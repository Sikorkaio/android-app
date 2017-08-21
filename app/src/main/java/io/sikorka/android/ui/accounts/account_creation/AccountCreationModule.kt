package io.sikorka.android.ui.accounts.account_creation

import toothpick.config.Module

class AccountCreationModule : Module() {
  init {
    bind(AccountCreationDialogPresenter::class.java)
        .to(AccountCreationDialogPresenterImpl::class.java)
        .singletonInScope()
  }
}