package io.sikorka.android.ui.wizard.slides.account_setup

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter

interface AccountSetupView : BaseView {
  fun setAccount(accountHex: String)
}

interface AccountSetupPresenter : Presenter<AccountSetupView> {
  fun loadAccount()
}