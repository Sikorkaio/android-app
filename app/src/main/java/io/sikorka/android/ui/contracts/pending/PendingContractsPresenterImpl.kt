package io.sikorka.android.ui.contracts.pending

import io.sikorka.android.data.contracts.pending.PendingContractDao
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.utils.schedulers.AppSchedulers
import javax.inject.Inject

@PendingContractsActivity.Presenter
class PendingContractsPresenterImpl
@Inject constructor(
  private val pendingContractDao: PendingContractDao,
  private val appSchedulers: AppSchedulers
) : PendingContractsPresenter, BasePresenter<PendingContractsView>() {
  override fun load() {
    addDisposable(pendingContractDao.getAllPendingContracts()
        .observeOn(appSchedulers.main)
        .subscribeOn(appSchedulers.io)
        .subscribe({
          attachedView().update(it)
        }) {
          attachedView().error(it.message)
        })
  }
}