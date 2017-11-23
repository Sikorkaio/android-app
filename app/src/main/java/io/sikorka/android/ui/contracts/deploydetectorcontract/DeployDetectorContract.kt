package io.sikorka.android.ui.contracts.deploydetectorcontract

import io.sikorka.android.core.contracts.data.ContractGas
import io.sikorka.android.core.contracts.data.DetectorContractData
import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module

interface DeployDetectorView : BaseView {
  fun showGasDialog(gas: ContractGas)
  fun showError(message: String)
  fun showError(code: Int)
  fun requestDeployAuthorization(gas: ContractGas)
  fun complete(hex: String)
}

interface DeployDetectorPresenter : Presenter<DeployDetectorView> {
  fun prepareGasSelection()
  fun prepareDeployWithDefaults()
  fun deployContract(passphrase: String, data: DetectorContractData)
}

class DeployDetectorModule : Module() {
  init {
    bind(DeployDetectorPresenter::class.java).to(DeployDetectorPresenterImpl::class.java).singletonInScope()
  }
}

