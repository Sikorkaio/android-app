package io.sikorka.android.ui.contracts

import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.GethNode
import io.sikorka.android.node.contracts.ContractData
import io.sikorka.android.node.contracts.ContractGas
import io.sikorka.android.node.contracts.ContractRepository
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class DeployContractPresenterImpl
@Inject
constructor(
    private val gethNode: GethNode,
    private val contractRepository: ContractRepository,
    private val schedulerProvider: SchedulerProvider
) : DeployContractPresenter, BasePresenter<DeployContractView>() {
  override fun load() {

    addDisposable(gethNode.suggestedGasPrice()
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.main())
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
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.main())
        .subscribe({
          attachedView().complete(it.address.hex)
        }) {
          attachedView().showError(it.message)
          Timber.v(it)
        }

  }

}