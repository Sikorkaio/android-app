package io.sikorka.android.ui.contracts.interact

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module


interface ContractInteractView : BaseView


interface ContractInteractPresenter : Presenter<ContractInteractView>


class ContractInteractModule : Module() {
  init {
    bind(ContractInteractPresenter::class.java).to(ContractInteractPresenterImpl::class.java)
  }
}