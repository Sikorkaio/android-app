package io.sikorka.android.ui.accounts.accountimport

import toothpick.config.Module

class AccountImportModule : Module() {
  init {
    bind(AccountImportPresenter::class.java)
        .to(AccountImportPresenterImpl::class.java)
        .singletonInScope()
  }
}
