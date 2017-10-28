package io.sikorka.android.ui.contracts.interact

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module


interface ContractInteractView : BaseView {
  fun showError()
  fun update(name: String)
  fun showConfirmationResult(confirmAnswer: Boolean)
  fun noDetector()
  fun detector(hex: String)
}


interface ContractInteractPresenter : Presenter<ContractInteractView> {
  fun load(contractAddress: String)
  fun verify(messageHex: String)
}


class ContractInteractModule : Module() {
  init {
    bind(ContractInteractPresenter::class.java).to(ContractInteractPresenterImpl::class.java)
  }
}