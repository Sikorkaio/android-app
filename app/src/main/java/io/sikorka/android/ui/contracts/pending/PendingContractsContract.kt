package io.sikorka.android.ui.contracts.pending

import io.sikorka.android.data.PendingContract
import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module


interface PendingContractsView: BaseView {
  fun update(data: List<PendingContract>)
  fun error(message: String?)
}

interface PendingContractsPresenter: Presenter<PendingContractsView> {
  fun load()
}

class PendingContractsModule : Module() {
  init {
    bind(PendingContractsPresenter::class.java).to(PendingContractsPresenterImpl::class.java).singletonInScope()
  }
}

