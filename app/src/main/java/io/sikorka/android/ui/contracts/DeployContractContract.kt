package io.sikorka.android.ui.contracts

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module

interface DeployContractView : BaseView {
  fun setSuggestedGasPrice(gasPrice: Double)
  fun requestDeployAuthorization(gasPrice: Long, gasLimit: Long)

}

interface DeployContractPresenter : Presenter<DeployContractView> {
  fun load()
  fun checkValues(gasPrice: Double, gasLimit: Double)
}


class DeployContractModule : Module() {
  init {
    bind(DeployContractPresenter::class.java).to(DeployContractPresenterImpl::class.java).singletonInScope()
  }
}