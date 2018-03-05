package io.sikorka.android.ui.wizard.slides.accountsetup

import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.settings.AppPreferences
import javax.inject.Inject

class AccountSetupPresenterImpl
@Inject
constructor(private val appPreferences: AppPreferences)
  : AccountSetupPresenter, BasePresenter<AccountSetupView>() {
  override fun loadAccount() {
    attachedView().setAccount(appPreferences.selectedAccount())
  }
}