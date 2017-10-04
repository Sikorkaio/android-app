package io.sikorka.android.ui.contracts.interact

import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.contracts.ContractRepository
import javax.inject.Inject

class ContractInteractPresenterImpl
@Inject
constructor(private val contractRepository: ContractRepository)
  : ContractInteractPresenter, BasePresenter<ContractInteractView>() {


}