package io.sikorka.android.ui.contracts.deploydetectorcontract

import io.sikorka.android.core.GethNode
import io.sikorka.android.data.contracts.ContractRepository
import io.sikorka.android.core.contracts.model.ContractGas
import io.sikorka.android.core.contracts.model.DetectorContractData
import io.sikorka.android.mvp.BasePresenter
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
    private val contractRepository: ContractRepository,
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
          attachedView().showError(it.message ?: "")
          Timber.e(it, "failed to get suggested gas price")
        }
    )
  }

  override fun deployContract(passphrase: String, data: DetectorContractData) {
    contractRepository.deployContract(passphrase, data)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.main())
        .subscribe({
          attachedView().complete(it.address.hex)
        }) {
          attachedView().showError(it.message ?: "")
          Timber.v(it)
        }
  }

  override fun prepareDeployWithDefaults() {
    val gasLimit = appPreferences.preferredGasLimit()
    val gasPrice = appPreferences.preferredGasPrice()

    if (gasLimit < 0 || gasPrice < 0) {
      attachedView().showError(DeployContractCodes.NO_GAS_PREFERENCES)
    } else {
      attachedView().requestDeployAuthorization(ContractGas(gasPrice, gasLimit))
    }
  }

}