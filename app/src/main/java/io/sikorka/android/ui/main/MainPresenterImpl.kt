package io.sikorka.android.ui.main

import android.arch.lifecycle.Observer
import io.sikorka.android.core.accounts.AccountModel
import io.sikorka.android.core.accounts.AccountRepository
import io.sikorka.android.core.monitor.ContractStatusEvent
import io.sikorka.android.core.monitor.TransactionStatusEvent
import io.sikorka.android.data.contracts.ContractRepository
import io.sikorka.android.data.location.UserLocation
import io.sikorka.android.data.location.UserLocationProvider
import io.sikorka.android.data.syncstatus.SyncStatusProvider
import io.sikorka.android.events.RxBus
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.utils.schedulers.AppSchedulers
import timber.log.Timber
import javax.inject.Inject

class MainPresenterImpl
@Inject
constructor(
  private val accountRepository: AccountRepository,
  private val contractRepository: ContractRepository,
  private val appSchedulers: AppSchedulers,
  private val locationProvider: UserLocationProvider,
  private val appPreferences: AppPreferences,
  syncStatusProvider: SyncStatusProvider,
  private val bus: RxBus
) : MainPresenter, BasePresenter<MainView>() {

  init {
    syncStatusProvider.observe(this, Observer {
      if (it == null) {
        return@Observer
      }
      attachedView().updateSyncStatus(it)
    })
    accountRepository.observeDefaultAccountBalance().observe(this, Observer {
      if (it == null) {
        return@Observer
      }
      val model = AccountModel(it.addressHex, it.balance)
      attachedView().updateAccountInfo(model, appPreferences.preferredBalancePrecision())
    })
    contractRepository.getDeployedContracts().observe(this, Observer {
      val data = it ?: return@Observer

      attachedView().updateDeployed(data)
    })
  }

  override fun userLocation(userLocation: UserLocation) {
    locationProvider.value = userLocation
  }

  override fun attach(view: MainView) {
    super.attach(view)
    bus.register(this, TransactionStatusEvent::class.java, {
      attachedView().notifyTransactionMined(it.txHash, it.success)
    })
    bus.register(this, ContractStatusEvent::class.java, {
      attachedView().notifyContractMined(it.address, it.txHash, it.success)
    })
  }

  override fun detach() {
    bus.unregister(this)
    super.detach()
  }

  override fun load() {
    addDisposable(accountRepository.selectedAccount()
        .subscribeOn(appSchedulers.io)
        .observeOn(appSchedulers.main)
        .subscribe({
          attachedView().updateAccountInfo(it, appPreferences.preferredBalancePrecision())
          Timber.v(it.toString())
        }) {
          Timber.v(it)
        }
    )
  }
}