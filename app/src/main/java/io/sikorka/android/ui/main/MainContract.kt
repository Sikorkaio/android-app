package io.sikorka.android.ui.main

import io.sikorka.android.core.accounts.AccountModel
import io.sikorka.android.core.contracts.model.DeployedContractModel
import io.sikorka.android.data.contracts.deployed.DeployedSikorkaContract
import io.sikorka.android.data.location.UserLocation
import io.sikorka.android.data.syncstatus.SyncStatus
import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module

interface MainView : BaseView {
  fun updateAccountInfo(model: AccountModel, preferredBalancePrecision: Int)
  fun updateSyncStatus(status: SyncStatus)
  fun loading(loading: Boolean)
  fun update(model: DeployedContractModel)
  fun error(error: Throwable)
  fun notifyTransactionMined(txHash: String, success: Boolean)
  fun notifyContractMined(address: String, txHash: String, success: Boolean)
  fun updateDeployed(data: List<DeployedSikorkaContract>)

}


interface MainPresenter : Presenter<MainView> {
  fun load()
  fun userLocation(userLocation: UserLocation)
}

class MainModule : Module() {
  init {
    bind(MainPresenter::class.java).to(MainPresenterImpl::class.java)
  }
}