package io.sikorka.android.ui.contracts

import io.reactivex.Scheduler
import io.sikorka.android.di.qualifiers.IoScheduler
import io.sikorka.android.di.qualifiers.MainScheduler
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.GethNode
import io.sikorka.android.node.etherToWei
import io.sikorka.android.node.toEther
import javax.inject.Inject

class DeployContractPresenterImpl
@Inject
constructor(
    private val gethNode: GethNode,
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

    view?.requestDeployAuthorization(gasPriceWei, gasLimitWei)

  }

}