package io.sikorka.android.node.monitor

import android.arch.lifecycle.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.toFlowable
import io.sikorka.android.data.PendingContractDao
import io.sikorka.android.data.syncstatus.SyncStatusProvider
import io.sikorka.android.events.RxBus
import io.sikorka.android.node.ethereumclient.LightClientProvider
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class PendingContractMonitor
@Inject constructor(
    private val lightClientProvider: LightClientProvider,
    private val syncStatusProvider: SyncStatusProvider,
    private val pendingContractDao: PendingContractDao,
    private val schedulerProvider: SchedulerProvider,
    private val bus: RxBus
) : LifecycleMonitor() {
  private val composite: CompositeDisposable = CompositeDisposable()
  private var onDeploymentStatusUpdateListener: OnDeploymentStatusUpdateListener? = null

  init {
    syncStatusProvider.observe(this, Observer {
      if (it == null || !it.syncing) {
        return@Observer
      }
      if (!lightClientProvider.initialized) {
        return@Observer
      }

      val lightClient = lightClientProvider.get()

      composite.add(pendingContractDao.getAllPendingContracts()
          .flatMap { it.toFlowable() }
          .flatMapSingle { lightClient.getTransactionReceipt(it.transactionHash) }
          .observeOn(schedulerProvider.io())
          .subscribeOn(schedulerProvider.io())
          .subscribe({
            pendingContractDao.deleteByContractAddress(it.contractAddressHex)
            onDeploymentStatusUpdateListener?.invoke(it)
            bus.post(ContractStatusEvent(it.contractAddressHex, it.txHash, it.successful))
          }) {
            Timber.e(it, "Db operation failed")
          })

    })
  }

  override fun stop() {
    super.stop()
    composite.clear()
  }

  fun setOnDeploymentStatusUpdateListener(onDeploymentStatuUpdateListener: OnDeploymentStatusUpdateListener?) {
    this.onDeploymentStatusUpdateListener = onDeploymentStatuUpdateListener
  }

}


