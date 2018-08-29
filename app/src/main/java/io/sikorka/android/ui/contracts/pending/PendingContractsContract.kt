package io.sikorka.android.ui.contracts.pending

import io.sikorka.android.data.contracts.pending.PendingContract
import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter

interface PendingContractsView : BaseView {
  fun update(data: List<PendingContract>)
  fun error(message: String?)
}

interface PendingContractsPresenter : Presenter<PendingContractsView> {
  fun load()
}