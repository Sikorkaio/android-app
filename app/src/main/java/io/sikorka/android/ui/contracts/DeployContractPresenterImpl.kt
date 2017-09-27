package io.sikorka.android.ui.contracts

import io.reactivex.Scheduler
import io.sikorka.android.di.qualifiers.IoScheduler
import io.sikorka.android.di.qualifiers.MainScheduler
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.ContractManager
import io.sikorka.android.node.GethNode
import io.sikorka.android.node.contracts.ContractData
import io.sikorka.android.node.contracts.ContractGas
import io.sikorka.android.node.etherToWei
import io.sikorka.android.node.toEther
import timber.log.Timber
import javax.inject.Inject

class DeployContractPresenterImpl
@Inject
constructor(
    private val gethNode: GethNode,
    private val contractManager: ContractManager,
    @IoScheduler private val ioScheduler: Scheduler,
    @MainScheduler private val mainThreadScheduler: Scheduler
) : DeployContractPresenter, BasePresenter<DeployContractView>() {
  override fun load() {

    addDisposable(gethNode.suggestedGasPrice()
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .subscribe({
          attachedView().setSuggestedGasPrice(it.toEther())
        }) {

        }
    )


  }

  override fun checkValues(gasPrice: Double, gasLimit: Double) {
    val gasPriceWei = etherToWei(gasPrice)
    val gasLimitWei = etherToWei(gasLimit)
    val contractGas = ContractGas(gasPriceWei, gasLimitWei)
    view?.requestDeployAuthorization(contractGas)
  }

  override fun deployContract(passphrase: String, contractInfo: ContractData) {
    contractManager.deployContract(passphrase, contractInfo)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .subscribe({
          attachedView().complete(it.Address.hex)
        }) {
          attachedView().showError(it.message)
          Timber.v(it)
        }

  }

}