package io.sikorka.android.ui.wizard.slides.accountsetup

import toothpick.config.Module

class AccountSetupModule : Module() {
  init {
    bind(AccountSetupPresenter::class.java).to(AccountSetupPresenterImpl::class.java).singletonInScope()
  }
}