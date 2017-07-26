package io.sikorka.test_geth.ui.accounts

import toothpick.config.Module

class AccountModule : Module() {
  init {
    bind(AccountPresenter::class.java).to(AccountPresenterImpl::class.java).singletonInScope()
  }
}