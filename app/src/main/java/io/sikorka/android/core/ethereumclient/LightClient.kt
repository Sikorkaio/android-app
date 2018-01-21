package io.sikorka.android.core.ethereumclient

import io.reactivex.Single
import io.sikorka.android.core.TransactionNotFoundException
import io.sikorka.android.core.model.TransactionReceipt
import io.sikorka.android.core.model.converters.GethReceiptConverter
import org.ethereum.geth.BoundContract
import org.ethereum.geth.Context
import org.ethereum.geth.EthereumClient
import org.ethereum.geth.Geth
import java.math.BigDecimal
import io.sikorka.android.core.model.Address as SikorkaAddress


class LightClient(
    private val ethereumClient: EthereumClient,
    private val context: Context
) {
  private val receiptConverter = GethReceiptConverter()

  fun getTransactionReceipt(txHashHex: String): Single<TransactionReceipt> = Single.fromCallable {
    val hash = Geth.newHashFromHex(txHashHex)
    val receipt = ethereumClient.getTransactionReceipt(context, hash)
    return@fromCallable receiptConverter.convert(receipt)
  }.onErrorResumeNext {
    val message = it.message ?: ""
    val throwable = if (message.contains("not found", true)) {
      TransactionNotFoundException(txHashHex, it)
    } else {
      it
    }
    return@onErrorResumeNext Single.error<TransactionReceipt>(throwable)
  }

  /**
   * Requests the balance for the specified account.
   */
  fun getBalance(address: SikorkaAddress): BigDecimal {
    val accountAddress = Geth.newAddressFromHex(address.hex)
    val bigIntBalance = ethereumClient.getBalanceAt(context, accountAddress, -1)
    return BigDecimal(bigIntBalance.getString(10))
  }

  fun <T> bindContract(address: String, abi: String, creator: (contract: BoundContract) -> T): T {
    return creator(Geth.bindContract(Geth.newAddressFromHex(address), abi, ethereumClient))
  }
}