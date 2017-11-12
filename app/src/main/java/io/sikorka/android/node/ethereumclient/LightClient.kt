package io.sikorka.android.node.ethereumclient

import io.reactivex.Single
import io.sikorka.android.node.TransactionNotFoundException
import org.ethereum.geth.Context
import org.ethereum.geth.EthereumClient
import org.ethereum.geth.Geth
import org.ethereum.geth.Receipt


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
}