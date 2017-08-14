package io.sikorka.test_geth.ui.accounts.account_export

import toothpick.config.Module

class AccountExportModule : Module() {
  init {
    bind(AccountExportPresenter::class.java).to(AccountExportPresenterImpl::class.java).singletonInScope()
  }
}