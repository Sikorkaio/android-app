package io.sikorka.android.ui.contracts

import io.reactivex.rxkotlin.plusAssign
import io.sikorka.android.core.GethNode
import io.sikorka.android.core.contracts.model.ContractData
import io.sikorka.android.core.contracts.model.ContractGas
import io.sikorka.android.data.contracts.ContractRepository
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.utils.schedulers.AppSchedulers
import timber.log.Timber

class DeployContractPresenterImpl(
  private val gethNode: GethNode,
  private val contractRepository: ContractRepository,
  private val appSchedulers: AppSchedulers,
  private val appPreferences: AppPreferences
) : DeployContractPresenter, BasePresenter<DeployContractView>() {

  override fun load() {

    addDisposable(gethNode.suggestedGasPrice()
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.main)
      .subscribe({
        attachedView().setSuggestedGasPrice(it.price)
      }) {
      }
    )
  }

  override fun deployContract(passphrase: String, contractInfo: ContractData) {
    disposables += contractRepository.deployContract(passphrase, contractInfo)
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.main)
      .subscribe({
        attachedView().complete(it.address.hex)
      }) {
        attachedView().showError(it.message)
        Timber.v(it)
      }
  }

  override fun prepareGasSelection() {
    addDisposable(gethNode.suggestedGasPrice()
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.main)
      .subscribe({
        attachedView().showGasDialog(it)
      }) {
        attachedView().showError(it.message)
      }
    )
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