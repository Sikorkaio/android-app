package io.sikorka.android.ui.main

import io.reactivex.Scheduler
import io.sikorka.android.di.qualifiers.IoScheduler
import io.sikorka.android.di.qualifiers.MainScheduler
import io.sikorka.android.events.RxBus
import io.sikorka.android.events.UpdateSyncStatusEvent
import io.sikorka.android.helpers.Lce
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.accounts.AccountRepository
import io.sikorka.android.node.contracts.ContractRepository
import io.sikorka.android.node.contracts.DeployedContract
import io.sikorka.android.node.contracts.DeployedContractModel
import timber.log.Timber
import javax.inject.Inject

class MainPresenterImpl
@Inject
constructor(
    private val accountRepository: AccountRepository,
    private val contractRepository: ContractRepository,
    private val bus: RxBus,
    @IoScheduler private val ioScheduler: Scheduler,
    @MainScheduler private val mainScheduler: Scheduler
) : MainPresenter, BasePresenter<MainView>() {

  override fun attach(view: MainView) {
    super.attach(view)
    bus.register(this, UpdateSyncStatusEvent::class.java, true) {
      this.view?.updateSyncStatus(it.syncStatus)
    }
  }

  override fun detach() {
    super.detach()
    bus.unregister(this)
  }

  override fun load(latitude: Double, longitude: Double) {
    addDisposable(accountRepository.selectedAccount()
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribe({
          attachedView().updateAccountInfo(it)
          Timber.v(it.toString())
        }) {
          Timber.v(it)
        }
    )
    loadDeployed(latitude, longitude)
  }

  fun loadDeployed(latitude: Double, longitude: Double) {
    addDisposable(contractRepository.getDeployedContracts()
        .toObservable()
        .startWith(Lce.loading())
        .onErrorReturn { Lce.failure(it) }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribe({
          Timber.v("Completed retrieving deployed contracts")
          when {
            it.success() -> attachedView().update(mockData(latitude, longitude))
            it.failure() -> attachedView().update(mockData(latitude, longitude))//attachedView().error(it.error())
            it.loading() -> attachedView().loading(true)
          }
        }) {
          Timber.v(it)
        }
    )
  }

  private fun mockData(latitude: Double, longitude: Double): DeployedContractModel {
    val deployedContracts = listOf(DeployedContract("0x1218418b656cb5b4a818275b7d7dc0948a4eb2ca", latitude - 0.004, longitude - 0.002),
        DeployedContract("0xfc69b76de6300d72237a643579b3216b3deee1ef", latitude - 0.006, longitude + 0.005),
        DeployedContract("0x567c98f000e88c477e87567f412b13ead25e8c90", latitude + 0.001, longitude - 0.008),
        DeployedContract("0xd438e5deb358c74e7b10251ee61694bc2436ed2f", latitude + 0.007, longitude + 0.002)
    )
    return DeployedContractModel(deployedContracts)
  }

}