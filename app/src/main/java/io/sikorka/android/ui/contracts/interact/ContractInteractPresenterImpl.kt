package io.sikorka.android.ui.contracts.interact

import io.reactivex.Completable
import io.sikorka.android.contract.SikorkaBasicInterfacev011
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.contracts.ContractRepository
import io.sikorka.android.utils.schedulers.SchedulerProvider
import org.ethereum.geth.Address
import timber.log.Timber
import java.math.BigInteger
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ContractInteractPresenterImpl
@Inject
constructor(
    private val contractRepository: ContractRepository,
    private val schedulerProvider: SchedulerProvider
) : ContractInteractPresenter, BasePresenter<ContractInteractView>() {

  private lateinit var boundInterface: SikorkaBasicInterfacev011
  private var usesDetector: Boolean = false

  override fun load(contractAddress: String) {
    addDisposable(contractRepository.bindSikorkaInterface(contractAddress)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.main())
        .subscribe({
          boundInterface = it
          attachedView().update(it.name())
          val detector = boundInterface.detector()
          usesDetector = detector.toInt() != 0
          if (!usesDetector) {
            attachedView().noDetector()
          } else {
            attachedView().detector(detector.hex)
          }
        }) {
          attachedView().showError()
        }
    )


  }

  override fun verify(messageHex: String) {
    addDisposable(Completable.timer(500, TimeUnit.MILLISECONDS)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.main()).subscribe({
      if (usesDetector) {
        attachedView().showConfirmationResult(true)
      } else {

      }
    }) {
      Timber.e(it, "Failed")
    })


  }

  fun Address.toInt(): Int {
    val hex = hex.replace("0x", "")
    val integer = BigInteger(hex, 16)
    return integer.toInt()
  }

}