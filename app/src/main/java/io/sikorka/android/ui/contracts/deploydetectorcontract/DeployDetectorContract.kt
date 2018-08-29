package io.sikorka.android.ui.contracts.deploydetectorcontract

import io.sikorka.android.core.contracts.model.ContractGas
import io.sikorka.android.core.contracts.model.DetectorContractData
import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter

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