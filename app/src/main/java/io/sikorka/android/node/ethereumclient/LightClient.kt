package io.sikorka.android.node.ethereumclient

import io.reactivex.Single
import io.sikorka.android.node.TransactionNotFoundException
import org.ethereum.geth.Context
import org.ethereum.geth.EthereumClient
import org.ethereum.geth.Geth
import org.ethereum.geth.Receipt
import java.math.BigDecimal
import io.sikorka.android.eth.Address as SikorkaAddress


class LightClient(
    private val ethereumClient: EthereumClient,
    private val context: Context
) {

  fun getTransactionReceipt(txHashHex: String): Single<Receipt> = Single.fromCallable {
    val hash = Geth.newHashFromHex(txHashHex)
    return@fromCallable ethereumClient.getTransactionReceipt(context, hash)
  }.onErrorResumeNext {
    val message = it.message ?: ""
    val throwable = if (message.contains("not found", true)) {
      TransactionNotFoundException(txHashHex, it)
    } else {
      it
    }
    return@onErrorResumeNext Single.error<Receipt>(throwable)
  }

  /**
   * Requests the balance for the specified account.
   */
  fun getBalance(address: SikorkaAddress): BigDecimal {
    val accountAddress = Geth.newAddressFromHex(address.hex)
    val bigIntBalance = ethereumClient.getBalanceAt(context, accountAddress, -1)
    return BigDecimal(bigIntBalance.getString(10))
  }
}