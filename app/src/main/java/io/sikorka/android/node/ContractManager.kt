package io.sikorka.android.node

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.sikorka.android.contract.SikorkaBasicInterface
import io.sikorka.android.helpers.fail
import io.sikorka.android.helpers.hexStringToByteArray
import io.sikorka.android.helpers.sha3.kekkac256
import io.sikorka.android.node.accounts.AccountRepository
import io.sikorka.android.node.accounts.InvalidPassphraseException
import io.sikorka.android.node.contracts.ContractData
import org.ethereum.geth.EthereumClient
import org.ethereum.geth.Geth
import org.ethereum.geth.TransactOpts
import timber.log.Timber
import javax.inject.Inject

class ContractManager
@Inject constructor(
    private val gethNode: GethNode,
    private val accountRepository: AccountRepository
) {

  fun deployContract(passphrase: String, contractData: ContractData): Single<SikorkaBasicInterface> {
    val sign = accountRepository.selectedAccount().flatMap {
      val gas = contractData.gas
      return@flatMap gethNode.createTransactOpts(it, gas.price, gas.limit) { _, transaction, chainId ->
        val signedTransaction = accountRepository.sign(it.addressHex, passphrase, transaction, chainId) ?: fail("null transaction was returned")
        Timber.v("sign ${transaction.hash.hex} ${transaction.cost} ${transaction.nonce}")

        signedTransaction
      }
    }
    return Single.zip(sign, gethNode.ethereumClient(), BiFunction { transactOpts: TransactOpts, ec: EthereumClient ->
      Timber.v("getting ready to deploy")
      DeployData(transactOpts, ec)
    }).flatMap {
      Single.fromCallable {
        val lat = Geth.newBigInt((contractData.latitude * 10000).toLong())
        val long = Geth.newBigInt((contractData.longitude * 10000).toLong())
        val answerHash = contractData.answer.kekkac256()
        val contractName = "sikorka experiment"
        val basicInterface = SikorkaBasicInterface.deploy(it.transactOpts, it.ec, contractName, lat, long, contractData.question, answerHash.hexStringToByteArray())
        Timber.v("preparing to deploy contract: {$contractName} with lat: ${lat.int64}, long ${long.int64} question: ${contractData.question} => answer hash: $answerHash")
        Timber.v("pending: ${basicInterface.Address.hex} -> ${basicInterface.Deployer.hash.hex}")
        basicInterface
      }.onErrorResumeNext {
        val message = it.message ?: ""
        when {
          message.contains("could not decrypt key with given passphrase") -> Single.error<SikorkaBasicInterface>(InvalidPassphraseException(it))
          message.contains("exceeds block gas limit") -> Single.error<SikorkaBasicInterface>(ExceedsBlockGasLimit(it))
          else -> return@onErrorResumeNext Single.error<SikorkaBasicInterface>(it)
        }
      }
    }

  }

  private data class DeployData(val transactOpts: TransactOpts, val ec: EthereumClient)
}

class ExceedsBlockGasLimit(cause: Throwable) : Exception(cause)
