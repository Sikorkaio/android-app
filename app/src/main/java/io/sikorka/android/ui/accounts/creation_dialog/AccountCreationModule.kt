package io.sikorka.android.ui.accounts.creation_dialog

import toothpick.config.Module

class AccountCreationModule : Module() {
  init {
    bind(AccountCreationDialogPresenter::class.java)
        .to(AccountCreationDialogPresenterImpl::class.java)
        .singletonInScope()
  }
}