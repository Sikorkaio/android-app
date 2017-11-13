package io.sikorka.android.node.monitor

import android.arch.lifecycle.Observer
import io.reactivex.Completable
import io.sikorka.android.data.balance.AccountBalance
import io.sikorka.android.data.balance.AccountBalanceDao
import io.sikorka.android.data.syncstatus.SyncStatusProvider
import io.sikorka.android.eth.Address
import io.sikorka.android.node.accounts.AccountRepository
import io.sikorka.android.node.ethereumclient.LightClient
import io.sikorka.android.node.ethereumclient.LightClientProvider
import io.sikorka.android.node.toEther
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class AccountBalanceMonitor
@Inject constructor(
    private val lightClientProvider: LightClientProvider,
    private val syncStatusProvider: SyncStatusProvider,
    private val accountBalanceDao: AccountBalanceDao,
    private val accountRepository: AccountRepository,
    private val schedulerProvider: SchedulerProvider
) : LifecycleMonitor() {

  init {
    syncStatusProvider.observe(this, Observer {
      accountRepository.getAccountAddresses().flatMapCompletable { address ->
        return@flatMapCompletable if (!lightClientProvider.initialized) {
          Completable.complete()
        } else {
          val client = lightClientProvider.get()
          updateBalance(client, address)
        }
      }.subscribeOn(schedulerProvider.db())
          .subscribe({
            Timber.v("balance was updated")
          }) { ex ->
            Timber.e(ex, "Failed to update balance")
          }
    })
  }

  private fun updateBalance(client: LightClient, accountAddress: Address) = Completable.fromCallable {
    val accountBalance = client.getBalance(accountAddress)
    val addressHex = accountAddress.hex
    val currentBalance = AccountBalance(addressHex = addressHex, balance = accountBalance.toEther())
    val previousBalance = accountBalanceDao.getBalance(addressHex)
    if (previousBalance.balance != currentBalance.balance) {
      accountBalanceDao.insert(currentBalance)
    }
  }
}