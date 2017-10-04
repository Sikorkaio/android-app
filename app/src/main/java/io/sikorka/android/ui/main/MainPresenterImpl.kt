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
    f()
  }

  override fun detach() {
    super.detach()
    bus.unregister(this)
  }

  override fun load() {
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

    addDisposable(contractRepository.getDeployedContracts()
        .toObservable()
        .startWith(Lce.loading())
        .onErrorReturn { Lce.failure(it) }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribe({
          when {
            it.success() -> attachedView().update(it.data())
            it.failure() -> attachedView().error(it.error())
            it.loading() -> attachedView().loading(true)
          }
        }) {
          Timber.v(it)
        }
    )
  }

  private fun f() {
    contractRepository.bindSikorkaInterface("0xDBdbf53199Ce386Ddc88185Ee7ec54c658703419")
  }



}