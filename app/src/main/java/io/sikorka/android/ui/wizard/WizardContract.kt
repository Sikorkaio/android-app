package io.sikorka.android.ui.wizard

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter

interface WizardView : BaseView {
  fun accountsExists(exists: Boolean)
}

interface WizardPresenter : Presenter<WizardView> {
  fun checkForDefaultAccount()
}