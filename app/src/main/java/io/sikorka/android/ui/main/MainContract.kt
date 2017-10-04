package io.sikorka.android.ui.main

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import io.sikorka.android.node.SyncStatus
import io.sikorka.android.node.accounts.AccountModel
import io.sikorka.android.node.contracts.DeployedContractModel
import toothpick.config.Module

interface MainView : BaseView {
  fun updateAccountInfo(model: AccountModel)
  fun updateSyncStatus(status: SyncStatus)
  fun loading(loading: Boolean)
  fun update(model: DeployedContractModel)
  fun error(error: Throwable)

}


interface MainPresenter : Presenter<MainView> {
  fun load()

}

class MainModule : Module() {
  init {
    bind(MainPresenter::class.java).to(MainPresenterImpl::class.java)
  }
}