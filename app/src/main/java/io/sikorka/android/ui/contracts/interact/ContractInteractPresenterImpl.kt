package io.sikorka.android.ui.contracts.interact

import io.reactivex.Scheduler
import io.sikorka.android.contract.ISikorkaBasicInterface
import io.sikorka.android.di.qualifiers.IoScheduler
import io.sikorka.android.di.qualifiers.MainScheduler
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.contracts.ContractRepository
import org.ethereum.geth.Address
import org.ethereum.geth.CallOpts
import org.ethereum.geth.TransactOpts
import org.ethereum.geth.Transaction
import javax.inject.Inject

class ContractInteractPresenterImpl
@Inject
constructor(
    private val contractRepository: ContractRepository,
    @IoScheduler private val ioScheduler: Scheduler,
    @MainScheduler private val mainScheduler: Scheduler
) : ContractInteractPresenter, BasePresenter<ContractInteractView>() {

  private lateinit var boundInterface: ISikorkaBasicInterface

  override fun load(contractAddress: String) {
    addDisposable(contractRepository.bindSikorkaInterface(contractAddress)
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .onErrorReturn {
          object : ISikorkaBasicInterface {
            override fun confirmAnswer(opts: CallOpts?, _answer: String): Boolean {
              return _answer == "Answer"
            }

            override fun name(opts: CallOpts?): String {
              return "Mock Contract"
            }

            override fun question(opts: CallOpts?): String {
              return "Mock Question"
            }

            override fun changeOwner(opts: TransactOpts, _newOwner: Address): Transaction {
              TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun changeQuestion(opts: TransactOpts, _question: String, _answer_hash: ByteArray): Transaction {
              TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
          }
        }
        .subscribe({
          boundInterface = it
          attachedView().update(it.question(), it.name())
        }) {
          attachedView().showError()
        }
    )
  }

  override fun confirmAnswer(answer: String) {
    val confirmAnswer = boundInterface.confirmAnswer(null, answer)
    attachedView().showConfirmationResult(confirmAnswer)
  }


}