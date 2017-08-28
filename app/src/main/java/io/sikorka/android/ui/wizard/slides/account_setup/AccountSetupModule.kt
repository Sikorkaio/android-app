package io.sikorka.android.ui.wizard.slides.account_setup

import toothpick.config.Module

class AccountSetupModule : Module() {
  init {
    bind(AccountSetupPresenter::class.java).to(AccountSetupPresenterImpl::class.java).singletonInScope()
  }
}