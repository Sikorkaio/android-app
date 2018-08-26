package io.sikorka.android.core.monitor

import androidx.lifecycle.Observer
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.sikorka.android.core.accounts.AccountRepository
import io.sikorka.android.core.ethereumclient.LightClient
import io.sikorka.android.core.ethereumclient.LightClientProvider
import io.sikorka.android.core.model.Address
import io.sikorka.android.core.toEther
import io.sikorka.android.data.balance.AccountBalance
import io.sikorka.android.data.balance.AccountBalanceDao
import io.sikorka.android.data.syncstatus.SyncStatusProvider
import io.sikorka.android.utils.isDisposed
import io.sikorka.android.utils.schedulers.AppSchedulers
import timber.log.Timber
import javax.inject.Inject

class AccountBalanceMonitor
@Inject constructor(
  private val lightClientProvider: LightClientProvider,
  syncStatusProvider: SyncStatusProvider,
  private val accountBalanceDao: AccountBalanceDao,
  private val accountRepository: AccountRepository,
  private val appSchedulers: AppSchedulers
) : LifecycleMonitor() {

  private var disposable: Disposable? = null

  init {
    syncStatusProvider.observe(this, Observer {

      if (it == null || !it.syncing) {
        return@Observer
      }
      if (!lightClientProvider.initialized) {
        return@Observer
      }

      if (!disposable.isDisposed()) {
        return@Observer
      }

      val client = lightClientProvider.get()

      disposable = accountRepository.getAccountAddresses()
        .subscribeOn(appSchedulers.io)
        .observeOn(appSchedulers.db)
        .subscribe({ address ->
          updateBalance(client, address)
        }) { ex ->
          Timber.e(ex, "Failed to update balance")
        }
    })
  }

  private fun updateBalance(client: LightClient, accountAddress: Address): Completable? {
    return Completable.fromCallable {
      val accountBalance = client.getBalance(accountAddress)
      val addressHex = accountAddress.hex

      val currentBalance = AccountBalance(
        addressHex = addressHex,
        balance = accountBalance.toEther()
      )

      val previousBalance = accountBalanceDao.getBalance(addressHex)
      val previousBalanceInfo = previousBalance?.balance
      val currentBalanceInfo = currentBalance.balance
      if (previousBalance == null || previousBalanceInfo != currentBalanceInfo) {
        Timber.v("updating balance from $previousBalanceInfo to $currentBalanceInfo")
        accountBalanceDao.insert(currentBalance)
      }
    }
  }
}