package io.sikorka.android.ui.contracts.interact

import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module


interface ContractInteractView : BaseView {
  fun showError()
  fun update(question: String, name: String)
  fun showConfirmationResult(confirmAnswer: Boolean)
}


interface ContractInteractPresenter : Presenter<ContractInteractView> {
  fun load(contractAddress: String)
  fun confirmAnswer(answer: String)
}


class ContractInteractModule : Module() {
  init {
    bind(ContractInteractPresenter::class.java).to(ContractInteractPresenterImpl::class.java)
  }
}