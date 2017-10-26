package io.sikorka.android.ui.contracts.deploydetectorcontract

import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.GethNode
import io.sikorka.android.node.contracts.ContractGas
import io.sikorka.android.node.contracts.DetectorContractData
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.ui.contracts.DeployContractCodes
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

@DeployDetectorActivity.Presenter
class DeployDetectorPresenterImpl
@Inject
constructor(
    private val gethNode: GethNode,
    private val schedulerProvider: SchedulerProvider,
    private val appPreferences: AppPreferences
) : DeployDetectorPresenter, BasePresenter<DeployDetectorView>() {
  override fun prepareGasSelection() {
    addDisposable(gethNode.suggestedGasPrice()
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.main())
        .subscribe({
          attachedView().showGasDialog(it)
        }) {
          attachedView().showError(it.message?:"")
          Timber.e(it, "failed to get suggested gas price")
        }
    )
  }

  override fun deployContract(passphrase: String, data: DetectorContractData) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun prepareDeployWithDefaults() {
    val gasLimit = appPreferences.preferredGasLimit()
    val gasPrice = appPreferences.preferredGasPrice()

    if (gasLimit < 0 || gasPrice < 0) {
      view?.showError(DeployContractCodes.NO_GAS_PREFERENCES)
    } else {
      view?.requestDeployAuthorization(ContractGas(gasPrice, gasLimit))
    }
  }

}