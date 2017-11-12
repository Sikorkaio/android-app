package io.sikorka.android.ui.main

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import io.sikorka.android.data.syncstatus.SyncStatus
import io.sikorka.android.node.accounts.AccountModel
import io.sikorka.android.node.contracts.data.DeployedContractModel
import toothpick.config.Module

interface MainView : BaseView {
  fun updateAccountInfo(model: AccountModel)
  fun updateSyncStatus(status: SyncStatus)
  fun loading(loading: Boolean)
  fun update(model: DeployedContractModel)
  fun error(error: Throwable)
  fun notifyTransactionMined(txHash: String, success: Boolean)
  fun notifyContractMined(address: String, txHash: String, success: Boolean)

}


interface MainPresenter : Presenter<MainView> {
  fun load()

}

class MainModule : Module() {
  init {
    bind(MainPresenter::class.java).to(MainPresenterImpl::class.java)
  }
}