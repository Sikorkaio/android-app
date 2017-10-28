package io.sikorka.android.ui.contracts.interact

import io.sikorka.android.contract.SikorkaBasicInterfacev011
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.contracts.ContractRepository
import io.sikorka.android.utils.schedulers.SchedulerProvider
import javax.inject.Inject

class ContractInteractPresenterImpl
@Inject
constructor(
    private val contractRepository: ContractRepository,
    private val schedulerProvider: SchedulerProvider
) : ContractInteractPresenter, BasePresenter<ContractInteractView>() {

  private lateinit var boundInterface: SikorkaBasicInterfacev011

  override fun load(contractAddress: String) {
    addDisposable(contractRepository.bindSikorkaInterface(contractAddress)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.main())
        .subscribe({
          boundInterface = it
          attachedView().update(it.name())
        }) {
          attachedView().showError()
        }
    )
  }

  override fun confirmAnswer(answer: String) {


  }


}