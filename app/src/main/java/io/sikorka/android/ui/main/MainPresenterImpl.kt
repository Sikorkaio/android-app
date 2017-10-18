package io.sikorka.android.ui.main

import io.sikorka.android.helpers.Lce
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.GethNode
import io.sikorka.android.node.accounts.AccountRepository
import io.sikorka.android.node.contracts.ContractRepository
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class MainPresenterImpl
@Inject
constructor(
    private val accountRepository: AccountRepository,
    private val contractRepository: ContractRepository,
    private val gethNode: GethNode,
    private val schedulerProvider: SchedulerProvider
) : MainPresenter, BasePresenter<MainView>() {

  override fun attach(view: MainView) {
    super.attach(view)
    addDisposable(gethNode.status()
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.main())
        .subscribe({
          attachedView().updateSyncStatus(it)
        }) {
          Timber.v(it, "Failed")
        }
    )
  }

  override fun load(latitude: Double, longitude: Double) {
    addDisposable(accountRepository.selectedAccount()
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.main())
        .subscribe({
          attachedView().updateAccountInfo(it)
          Timber.v(it.toString())
        }) {
          Timber.v(it)
        }
    )
    loadDeployed()
  }

  private fun loadDeployed() {
    addDisposable(contractRepository.getDeployedContracts()
        .toObservable()
        .startWith(Lce.loading())
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.main())
        .doOnTerminate {
          Timber.v("termin")
          attachedView().loading(false)
        }
        .subscribe({
          Timber.v("Deployed retrieval completed ${it.success()}")
          when {
            it.success() -> attachedView().update(it.data())
            it.failure() -> {
              attachedView().error(it.error())
              Timber.v(it.error())
            }
            it.loading() -> attachedView().loading(true)
          }
        }) {
          Timber.v(it)
        }
    )
  }

}