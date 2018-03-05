package io.sikorka.android.ui.accounts.accountcreation

import toothpick.config.Module

class AccountCreationModule : Module() {
  init {
    bind(AccountCreationDialogPresenter::class.java)
        .to(AccountCreationDialogPresenterImpl::class.java)
        .singletonInScope()
  }
}