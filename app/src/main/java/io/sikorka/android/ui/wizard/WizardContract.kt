package io.sikorka.android.ui.wizard

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module

interface WizardView : BaseView {
  fun accountsExists(exists: Boolean)
}

interface WizardPresenter : Presenter<WizardView> {
  fun checkForDefaultAccount()
}

class WizardModule : Module() {
  init {
    bind(WizardPresenter::class.java).to(WizardPresenterImpl::class.java).singletonInScope()
  }
}