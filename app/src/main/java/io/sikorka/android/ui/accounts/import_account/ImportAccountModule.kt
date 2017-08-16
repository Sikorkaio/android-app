package io.sikorka.android.ui.accounts.import_account

import toothpick.config.Module

class ImportAccountModule : Module() {
  init {
    bind(ImportAccountPresenter::class.java)
        .to(ImportAccountPresenterImpl::class.java)
        .singletonInScope()
  }
}
