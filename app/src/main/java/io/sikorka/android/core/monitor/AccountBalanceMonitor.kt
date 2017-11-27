package io.sikorka.android.core.monitor

import android.arch.lifecycle.Observer
import io.reactivex.Completable
import io.sikorka.android.core.accounts.AccountRepository
import io.sikorka.android.core.ethereumclient.LightClient
import io.sikorka.android.core.ethereumclient.LightClientProvider
import io.sikorka.android.core.model.Address
import io.sikorka.android.core.toEther
import io.sikorka.android.data.balance.AccountBalance
import io.sikorka.android.data.balance.AccountBalanceDao
import io.sikorka.android.data.syncstatus.SyncStatusProvider
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class AccountBalanceMonitor
@Inject constructor(
    private val lightClientProvider: LightClientProvider,
    syncStatusProvider: SyncStatusProvider,
    private val accountBalanceDao: AccountBalanceDao,
    private val accountRepository: AccountRepository,
    private val schedulerProvider: SchedulerProvider
) : LifecycleMonitor() {

  init {
    syncStatusProvider.observe(this, Observer {

      if (it == null || !it.syncing) {
        return@Observer
      }

      accountRepository.getAccountAddresses().flatMapCompletable { address ->
        return@flatMapCompletable if (!lightClientProvider.initialized) {
          Completable.complete()
        } else {
          val client = lightClientProvider.get()
          updateBalance(client, address)
        }
      }.subscribeOn(schedulerProvider.db())
          .subscribe({ }) { ex ->
            Timber.e(ex, "Failed to update balance")
          }
    })
  }

  private fun updateBalance(client: LightClient, accountAddress: Address) = Completable.fromCallable {
    val accountBalance = client.getBalance(accountAddress)
    val addressHex = accountAddress.hex
    val currentBalance = AccountBalance(addressHex = addressHex, balance = accountBalance.toEther())
    val previousBalance = accountBalanceDao.getBalance(addressHex)
    if (previousBalance == null || previousBalance.balance != currentBalance.balance) {
      accountBalanceDao.insert(currentBalance)
    }
  }
}