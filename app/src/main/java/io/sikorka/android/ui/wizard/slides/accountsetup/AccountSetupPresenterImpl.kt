package io.sikorka.android.ui.wizard.slides.accountsetup

import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.settings.AppPreferences

class AccountSetupPresenterImpl(private val appPreferences: AppPreferences)
  : AccountSetupPresenter, BasePresenter<AccountSetupView>() {
  override fun loadAccount() {
    attachedView().setAccount(appPreferences.selectedAccount())
  }
}