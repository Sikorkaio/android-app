package io.sikorka.test_geth.ui.accounts.import_account

import toothpick.config.Module

class ImportAccountModule : Module() {
  init {
    bind(ImportAccountPresenter::class.java)
        .to(ImportAccountPresenterImpl::class.java)
        .singletonInScope()
  }
}
