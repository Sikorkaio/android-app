package io.sikorka.android.ui.main

import androidx.lifecycle.Observer
import io.sikorka.android.core.accounts.AccountModel
import io.sikorka.android.core.accounts.AccountRepository
import io.sikorka.android.core.monitor.ContractStatus
import io.sikorka.android.core.monitor.TransactionStatus
import io.sikorka.android.data.contracts.ContractRepository
import io.sikorka.android.data.location.UserLocation
import io.sikorka.android.data.location.UserLocationProvider
import io.sikorka.android.data.syncstatus.SyncStatusProvider
import io.sikorka.android.events.EventLiveDataProvider
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.utils.schedulers.AppSchedulers
import timber.log.Timber

class MainPresenterImpl(
  private val accountRepository: AccountRepository,
  contractRepository: ContractRepository,
  private val appSchedulers: AppSchedulers,
  private val locationProvider: UserLocationProvider,
  private val appPreferences: AppPreferences,
  syncStatusProvider: SyncStatusProvider,
  private val bus: EventLiveDataProvider
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
    bus.observe(this) {
      val content = it.getContentIfNotHandled() ?: return@observe

      when (content) {
        is ContractStatus -> {
          attachedView().notifyContractMined(content.address, content.txHash, content.success)
        }
        is TransactionStatus -> {
          attachedView().notifyTransactionMined(content.txHash, content.success)
        }
      }
    }
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