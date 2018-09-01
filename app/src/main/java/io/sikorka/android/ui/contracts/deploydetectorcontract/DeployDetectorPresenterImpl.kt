package io.sikorka.android.ui.contracts.deploydetectorcontract

import io.reactivex.rxkotlin.plusAssign
import io.sikorka.android.core.GethNode
import io.sikorka.android.core.contracts.model.ContractGas
import io.sikorka.android.core.contracts.model.DetectorContractData
import io.sikorka.android.data.contracts.ContractRepository
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.ui.contracts.DeployContractCodes
import io.sikorka.android.utils.schedulers.AppSchedulers
import timber.log.Timber

class DeployDetectorPresenterImpl(
  private val gethNode: GethNode,
  private val contractRepository: ContractRepository,
  private val appSchedulers: AppSchedulers,
  private val appPreferences: AppPreferences
) : DeployDetectorPresenter, BasePresenter<DeployDetectorView>() {
  override fun prepareGasSelection() {
    addDisposable(gethNode.suggestedGasPrice()
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.main)
      .subscribe({
        attachedView().showGasDialog(it)
      }) {
        attachedView().showError(it.message ?: "")
        Timber.e(it, "failed to get suggested gas price")
      }
    )
  }

  override fun deployContract(passphrase: String, data: DetectorContractData) {
    disposables += contractRepository.deployContract(passphrase, data)
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.main)
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