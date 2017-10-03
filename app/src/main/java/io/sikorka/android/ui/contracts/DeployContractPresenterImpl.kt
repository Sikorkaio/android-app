package io.sikorka.android.ui.contracts

import io.reactivex.Scheduler
import io.sikorka.android.di.qualifiers.IoScheduler
import io.sikorka.android.di.qualifiers.MainScheduler
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.GethNode
import io.sikorka.android.node.contracts.ContractData
import io.sikorka.android.node.contracts.ContractGas
import io.sikorka.android.node.contracts.ContractRepository
import timber.log.Timber
import javax.inject.Inject

class DeployContractPresenterImpl
@Inject
constructor(
    private val gethNode: GethNode,
    private val contractRepository: ContractRepository,
    @IoScheduler private val ioScheduler: Scheduler,
    @MainScheduler private val mainThreadScheduler: Scheduler
) : DeployContractPresenter, BasePresenter<DeployContractView>() {
  override fun load() {

    addDisposable(gethNode.suggestedGasPrice()
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .subscribe({
          attachedView().setSuggestedGasPrice(it.int64)
        }) {

        }
    )


  }

  override fun checkValues(gasPrice: Long, gasLimit: Long) {
    val contractGas = ContractGas(gasPrice, gasLimit)
    view?.requestDeployAuthorization(contractGas)
  }

  override fun deployContract(passphrase: String, contractInfo: ContractData) {
    contractRepository.deployContract(passphrase, contractInfo)
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