package io.sikorka.android.core.monitor

import androidx.lifecycle.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.toFlowable
import io.sikorka.android.core.ethereumclient.LightClientProvider
import io.sikorka.android.data.contracts.pending.PendingContractDao
import io.sikorka.android.data.syncstatus.SyncStatusProvider
import io.sikorka.android.events.Event
import io.sikorka.android.events.EventLiveDataProvider
import io.sikorka.android.utils.isDisposed
import io.sikorka.android.utils.schedulers.AppSchedulers
import timber.log.Timber

class PendingContractMonitor(
  syncStatusProvider: SyncStatusProvider,
  private val lightClientProvider: LightClientProvider,
  private val pendingContractDao: PendingContractDao,
  private val appSchedulers: AppSchedulers,
  private val bus: EventLiveDataProvider
) : LifecycleMonitor() {
  private var disposable: Disposable? = null
  private var statusUpdateListener: statusUpdateListener? = null

  init {
    syncStatusProvider.observe(this, Observer { status ->
      if (status == null || !status.syncing) {
        return@Observer
      }

      if (!lightClientProvider.initialized) {
        return@Observer
      }

      if (!disposable.isDisposed()) {
        return@Observer
      }

      val lightClient = lightClientProvider.get()

      disposable = pendingContractDao.getAllPendingContracts()
        .flatMap { it.toFlowable() }
        .flatMapSingle { pending ->
          lightClient.getTransactionReceipt(pending.transactionHash)
            .map { it.withContractAddress(pending.contractAddress) }
        }
        .observeOn(appSchedulers.monitor)
        .subscribeOn(appSchedulers.monitor)
        .subscribe({
          pendingContractDao.deleteByContractAddress(it.contractAddress())
          statusUpdateListener?.invoke(it)
          bus.post(Event(ContractStatus(it.contractAddress(), it.txHash, it.successful)))
        }) {
          Timber.e(it, "Db operation failed")
        }
    })
  }

  override fun stop() {
    super.stop()
    disposable?.dispose()
  }

  fun setStatusUpdateListener(
    statusUpdateListener: statusUpdateListener?
  ) {
    this.statusUpdateListener = statusUpdateListener
  }
}