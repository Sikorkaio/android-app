package io.sikorka.android.ui.contracts.interact

import io.sikorka.android.core.contracts.model.ContractGas
import io.sikorka.android.mvp.BaseView
import io.sikorka.android.mvp.Presenter
import toothpick.config.Module


interface ContractInteractView : BaseView {
  fun showError()
  fun update(name: String)
  fun showConfirmationResult(confirmAnswer: Boolean)
  fun noDetector()
  fun detector(hex: String)
  fun showGasSelection(gas: ContractGas)
  fun startDetectorFlow()
}


interface ContractInteractPresenter : Presenter<ContractInteractView> {
  fun load(contractAddress: String)
  fun verify()
  fun prepareGasSelection()
  fun cacheGas(gas: ContractGas)
  fun cachePassPhrase(passphrase: String)
  fun startClaimFlow()
  fun cacheMessage(detectorSignedMessage: String?)
}


class ContractInteractModule : Module() {
  init {
    bind(ContractInteractPresenter::class.java).to(ContractInteractPresenterImpl::class.java)
  }
}