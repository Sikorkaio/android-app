package io.sikorka.android.ui.accounts.account_export

import toothpick.config.Module

class AccountExportModule : Module() {
  init {
    bind(AccountExportPresenter::class.java).to(AccountExportPresenterImpl::class.java).singletonInScope()
  }
}