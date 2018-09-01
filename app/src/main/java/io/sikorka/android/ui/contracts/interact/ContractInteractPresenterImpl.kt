package io.sikorka.android.ui.contracts.interact

import io.reactivex.rxkotlin.plusAssign
import io.sikorka.android.contract.DiscountContract
import io.sikorka.android.core.GethNode
import io.sikorka.android.core.contracts.model.ContractGas
import io.sikorka.android.data.contracts.ContractRepository
import io.sikorka.android.data.transactions.PendingTransaction
import io.sikorka.android.data.transactions.PendingTransactionDao
import io.sikorka.android.helpers.hexStringToByteArray
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.utils.schedulers.AppSchedulers
import org.ethereum.geth.Address
import org.threeten.bp.Instant.now
import timber.log.Timber
import java.math.BigInteger

class ContractInteractPresenterImpl(
  private val contractRepository: ContractRepository,
  private val appSchedulers: AppSchedulers,
  private val gethNode: GethNode,
  private val pendingTransactionDao: PendingTransactionDao
) : ContractInteractPresenter, BasePresenter<ContractInteractView>() {

  private var gas: ContractGas? = null
  private var passphrase: String? = null
  private var detectorMessage: String? = null

  override fun cacheGas(gas: ContractGas) {
    this.gas = gas
  }

  override fun cachePassPhrase(passphrase: String) {
    this.passphrase = passphrase
  }

  override fun cacheMessage(detectorSignedMessage: String?) {
    this.detectorMessage = detectorSignedMessage
  }

  private lateinit var boundInterface: DiscountContract
  private var usesDetector: Boolean = false

  override fun load(contractAddress: String) {
    addDisposable(contractRepository.bindSikorkaInterface(contractAddress)
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.main)
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

  override fun verify() {

    val data = if (usesDetector) {
      detectorMessage?.hexStringToByteArray() ?: kotlin.ByteArray(0)
    } else {
      kotlin.ByteArray(0)
    }

    ifNotNull(gas, passphrase) { gas, passphrase ->
      disposables += contractRepository.transact({
        boundInterface.claimToken(it, data)
      }, passphrase, gas)
        .subscribeOn(appSchedulers.io)
        .observeOn(appSchedulers.main)
        .subscribe({
          attachedView().showConfirmationResult(true)
          pendingTransactionDao.insert(PendingTransaction(
            txHash = it.hash.hex,
            dateAdded = now().epochSecond
          ))
        }) {
          Timber.e(it, "failed")
        }
    }
  }

  override fun startClaimFlow() {
    if (usesDetector) {
      attachedView().startDetectorFlow()
    } else {
      prepareGasSelection()
    }
  }

  private fun ifNotNull(
    gas: ContractGas?,
    passphrase: String?,
    action: (gas: ContractGas, passphrase: String) -> Unit
  ): Boolean {
    return if (gas != null && passphrase != null) {
      action(gas, passphrase)
      true
    } else {
      false
    }
  }

  override fun prepareGasSelection() {
    addDisposable(gethNode.suggestedGasPrice()
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.main)
      .subscribe({
        attachedView().showGasSelection(it)
      }) {
        attachedView().showError()
      }
    )
  }

  private fun Address.toInt(): Int {
    val hex = hex.replace("0x", "")
    val integer = BigInteger(hex, 16)
    return integer.toInt()
  }
}