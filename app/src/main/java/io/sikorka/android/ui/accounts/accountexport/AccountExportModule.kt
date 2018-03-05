package io.sikorka.android.ui.accounts.accountexport

import toothpick.config.Module

class AccountExportModule : Module() {
  init {
    bind(AccountExportPresenter::class.java).to(AccountExportPresenterImpl::class.java).singletonInScope()
  }
}