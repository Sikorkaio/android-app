package io.sikorka.android.node.monitor

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.toObservable
import io.sikorka.android.data.PendingContractDao
import io.sikorka.android.events.RxBus
import io.sikorka.android.node.GethNode
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class DeployedContractMonitor
@Inject constructor(
    private val node: GethNode,
    private val pendingContractDao: PendingContractDao,
    private val schedulerProvider: SchedulerProvider,
    private val bus: RxBus
) {
  private val composite: CompositeDisposable = CompositeDisposable()
  private var onDeploymentStatusUpdateListener: OnDeploymentStatusUpdateListener? = null

  fun start() {
    composite.add(node.observeHeaders()
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.io())
        .flatMap { pendingContractDao.getAllPendingContracts().toObservable() }
        .flatMap { it.toObservable() }
        .subscribe({ contract ->
          node.getReceipt(contract.transactionHash).subscribe({
            val status = it.string()
            var success = false
            if (status.contains("status=1")) {
              pendingContractDao.delete(contract)
              onDeploymentStatusUpdateListener?.invoke(true, contract.contractAddress, contract.transactionHash)
              success = true
            } else if (status.contains("status=0")) {
              pendingContractDao.delete(contract)
              onDeploymentStatusUpdateListener?.invoke(false, contract.contractAddress, contract.transactionHash)
            }
            bus.post(ContractStatusEvent(contract.contractAddress, contract.transactionHash, success))
            Timber.v(status)

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


