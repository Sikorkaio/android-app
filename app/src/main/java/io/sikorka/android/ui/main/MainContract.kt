package io.sikorka.android.ui.main

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import io.sikorka.android.node.SyncStatus
import io.sikorka.android.node.accounts.AccountModel
import toothpick.config.Module

interface MainView : BaseView {
  fun updateAccountInfo(model: AccountModel)
  fun updateSyncStatus(status: SyncStatus)

}


interface MainPresenter : Presenter<MainView> {
  fun loadAccountInfo()

}

class MainModule : Module() {
  init {
    bind(MainPresenter::class.java).to(MainPresenterImpl::class.java)
  }
}