package io.sikorka.android.node.monitor

import android.arch.lifecycle.Observer
import io.sikorka.android.data.PendingTransactionDao
import io.sikorka.android.data.syncstatus.SyncStatusProvider
import io.sikorka.android.node.ethereumclient.LightClientProvider
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class PendingTransactionMonitor
@Inject
constructor(
    private val syncStatusProvider: SyncStatusProvider,
    private val pendingTransactionDao: PendingTransactionDao,
    private val schedulerProvider: SchedulerProvider,
    private val lightClientProvider: LightClientProvider
) : LifecycleMonitor() {

  override fun start() {
    super.start()
    syncStatusProvider.observe(this, Observer {

      if (!lightClientProvider.initialized) {
        Timber.v("No light client available yet")
        return@Observer
      }

      val lightClient = lightClientProvider.get()

      pendingTransactionDao.pendingTransaction()
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.io())
          .flatMapIterable { it }
          .toObservable()
          .flatMapSingle { lightClient.getTransactionReceipt(it.txHash) }
          .subscribe({

          }) {
            Timber.e(it, "Failure while processing pending transactions")
          }


    })
  }
}

