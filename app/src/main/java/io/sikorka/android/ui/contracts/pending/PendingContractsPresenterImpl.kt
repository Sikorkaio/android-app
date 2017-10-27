package io.sikorka.android.ui.contracts.pending

import io.sikorka.android.data.PendingContractDataSource
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.utils.schedulers.SchedulerProvider
import javax.inject.Inject

@PendingContractsActivity.Presenter
class PendingContractsPresenterImpl
@Inject constructor(
    private val pendingContractDataSource: PendingContractDataSource,
    private val schedulerProvider: SchedulerProvider
) : PendingContractsPresenter, BasePresenter<PendingContractsView>() {
  override fun load() {
    addDisposable(pendingContractDataSource.getAllPendingContracts()
        .toList()
        .observeOn(schedulerProvider.main())
        .subscribeOn(schedulerProvider.io())
        .subscribe({
          attachedView().update(it)
        }) {
          attachedView().error(it.message)
        })


  }

}