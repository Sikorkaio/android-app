package io.sikorka.android.node

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.sikorka.android.contract.SikorkaBasicInterface
import io.sikorka.android.helpers.fail
import io.sikorka.android.node.accounts.AccountRepository
import io.sikorka.android.node.contracts.ContractData
import org.ethereum.geth.EthereumClient
import org.ethereum.geth.Geth
import org.ethereum.geth.TransactOpts
import timber.log.Timber
import java.security.MessageDigest
import javax.inject.Inject

class ContractManager
@Inject constructor(
    private val gethNode: GethNode,
    private val accountRepository: AccountRepository
) {

  fun deployContract(passphrase: String, contractData: ContractData): Single<SikorkaBasicInterface> {
    val sign = accountRepository.selectedAccount().flatMap {
      val gas = contractData.gas
      return@flatMap gethNode.createTransactOpts(it.account, gas.price, gas.limit) { _, transaction, chainId ->
        Timber.v("Preparing to sign ${transaction.sigHash.hex} ${transaction.cost}")
        accountRepository.sign(it.account, passphrase, transaction, chainId) ?: fail("null transaction was returned")
      }
    }
    return Single.zip(sign, gethNode.ethereumClient(), BiFunction { transactOpts: TransactOpts, ec: EthereumClient ->
      Timber.v("getting ready to deploy")
      DeployData(transactOpts, ec)
    }).flatMap {
      Single.fromCallable {
        Timber.v("deploying in order")
        val lat = Geth.newBigInt((contractData.latitude * 10000).toLong())
        val long = Geth.newBigInt((contractData.longitude * 10000).toLong())
        val answerHash = contractData.answer.md5()
        SikorkaBasicInterface.deploy(it.transactOpts, it.ec, "sikorka experiment", lat, long, contractData.question, answerHash.toByteArray())
      }
    }

  }

  private fun String.md5(): String {
    val HEX_CHARS = "0123456789ABCDEF"
    val bytes = MessageDigest
        .getInstance("MD5")
        .digest(toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
      val i = it.toInt()
      result.append(HEX_CHARS[i shr 4 and 0x0f])
      result.append(HEX_CHARS[i and 0x0f])
    }

    return result.toString()
  }

  private data class DeployData(val transactOpts: TransactOpts, val ec: EthereumClient)
}