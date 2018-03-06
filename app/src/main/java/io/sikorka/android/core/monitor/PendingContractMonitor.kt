package io.sikorka.android.core.monitor

import android.arch.lifecycle.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.toFlowable
import io.sikorka.android.core.ethereumclient.LightClientProvider
import io.sikorka.android.data.contracts.pending.PendingContractDao
import io.sikorka.android.data.syncstatus.SyncStatusProvider
import io.sikorka.android.events.RxBus
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class PendingContractMonitor
@Inject constructor(
  syncStatusProvider: SyncStatusProvider,
  private val lightClientProvider: LightClientProvider,
  private val pendingContractDao: PendingContractDao,
  private val schedulerProvider: SchedulerProvider,
  private val bus: RxBus
) : LifecycleMonitor() {
  private val disposables: CompositeDisposable = CompositeDisposable()
  private var statusUpdateListener: statusUpdateListener? = null

  init {
    syncStatusProvider.observe(this, Observer {
      if (it == null || !it.syncing) {
        return@Observer
      }
      if (!lightClientProvider.initialized) {
        return@Observer
      }

      val lightClient = lightClientProvider.get()

      disposables.add(pendingContractDao.getAllPendingContracts()
        .flatMap { it.toFlowable() }
        .flatMapSingle { pending ->
          lightClient.getTransactionReceipt(pending.transactionHash)
            .map { it.withContractAddress(pending.contractAddress) }
        }
        .observeOn(schedulerProvider.monitor())
        .subscribeOn(schedulerProvider.monitor())
        .subscribe({
          pendingContractDao.deleteByContractAddress(it.contractAddress())
          statusUpdateListener?.invoke(it)
          bus.post(ContractStatusEvent(it.contractAddress(), it.txHash, it.successful))
        }) {
          Timber.e(it, "Db operation failed")
        })
    })
  }

  override fun stop() {
    super.stop()
    disposables.clear()
  }

  fun setStatusUpdateListener(
    statusUpdateListener: statusUpdateListener?
  ) {
    this.statusUpdateListener = statusUpdateListener
  }
}