package io.sikorka.android.node.contracts

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.toObservable
import io.sikorka.android.data.PendingContractDataSource
import io.sikorka.android.node.GethNode
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class DeployedContractMonitor
@Inject constructor(
    private val node: GethNode,
    private val pendingContractDataSource: PendingContractDataSource,
    private val schedulerProvider: SchedulerProvider
) {
  private val composite: CompositeDisposable = CompositeDisposable()
  private var onDeploymentStatusUpdateListener: OnDeploymentStatusUpdateListener? = null

  fun start() {
    composite.add(node.observeHeaders()
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.io())
        .flatMap { pendingContractDataSource.getAllPendingContracts().toObservable() }
        .flatMap { it.toObservable() }
        .subscribe({ contract ->
          node.getReceipt(contract.transactionHash).subscribe({
            if (it.string().contains("status=1")) {
              pendingContractDataSource.delete(contract)
              onDeploymentStatusUpdateListener?.invoke(true, contract.contractAddress, contract.transactionHash)
            }
            Timber.v(it.string())
          }) {
            Timber.v(it, "failed")
          }
        }) {
          Timber.e(it, "Failed ")
        }
    )

  }

  fun stop() {
    composite.clear()
  }


  fun setOnDeploymentStatusUpdateListener(onDeploymentStatuUpdateListener: OnDeploymentStatusUpdateListener?) {
    this.onDeploymentStatusUpdateListener = onDeploymentStatuUpdateListener
  }

}

typealias OnDeploymentStatusUpdateListener = (status: Boolean, contractAddress: String, txHash: String) -> Unit