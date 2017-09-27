package io.sikorka.android.ui.contracts

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import io.sikorka.android.node.contracts.ContractData
import io.sikorka.android.node.contracts.ContractGas
import toothpick.config.Module

interface DeployContractView : BaseView {
  fun setSuggestedGasPrice(gasPrice: Double)
  fun requestDeployAuthorization(gas: ContractGas)
  fun showError(message: String?)
  fun complete(hex: String?)

}

interface DeployContractPresenter : Presenter<DeployContractView> {
  fun load()
  fun checkValues(gasPrice: Double, gasLimit: Double)
  fun deployContract(passphrase: String, contractInfo: ContractData)
}


class DeployContractModule : Module() {
  init {
    bind(DeployContractPresenter::class.java).to(DeployContractPresenterImpl::class.java).singletonInScope()
  }
}