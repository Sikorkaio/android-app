package io.sikorka.android.node.contracts

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.sikorka.android.contract.ISikorkaBasicInterface
import io.sikorka.android.contract.SikorkaBasicInterface
import io.sikorka.android.contract.SikorkaRegistry
import io.sikorka.android.data.PendingContract
import io.sikorka.android.data.PendingContractDataSource
import io.sikorka.android.helpers.Lce
import io.sikorka.android.helpers.fail
import io.sikorka.android.helpers.hexStringToByteArray
import io.sikorka.android.helpers.sha3.kekkac256
import io.sikorka.android.node.ExceedsBlockGasLimit
import io.sikorka.android.node.GethNode
import io.sikorka.android.node.accounts.AccountRepository
import io.sikorka.android.node.accounts.InvalidPassphraseException
import org.ethereum.geth.EthereumClient
import org.ethereum.geth.Geth
import org.ethereum.geth.TransactOpts
import timber.log.Timber
import javax.inject.Inject


class ContractRepository
@Inject
constructor(
    private val gethNode: GethNode,
    private val accountRepository: AccountRepository,
    private val pendingContractDataSource: PendingContractDataSource
) {

  fun getDeployedContracts(): Single<Lce<DeployedContractModel>> = gethNode.ethereumClient()
      .flatMap { ethereumClient ->
        return@flatMap Single.fromCallable {
          val sikorkaRegistry = SikorkaRegistry.bind(ethereumClient)

          val contractAddresses = sikorkaRegistry.getContractAddresses()
          val contractCoordinates = sikorkaRegistry.getContractCoordinates()

          val contractList = ArrayList<DeployedContract>()

          for (i in 0 until contractCoordinates.size() step 2) {
            val address = contractAddresses[i / 2]
            val latitude = contractCoordinates[i].int64 / 10000.0
            val longitude = contractCoordinates[i + 1].int64 / 10000.0
            val deployedContract = DeployedContract(address.hex, latitude, longitude)
            contractList.add(deployedContract)
          }

          return@fromCallable Lce.success(DeployedContractModel(contractList))
        }
      }


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
    }).flatMap { deploy(contractData, it) }
  }

  private fun deploy(contractData: ContractData, deployData: DeployData): Single<SikorkaBasicInterface> = Single.fromCallable {
    val lat = Geth.newBigInt((contractData.latitude * 10000).toLong())
    val long = Geth.newBigInt((contractData.longitude * 10000).toLong())
    val answerHash = contractData.answer.kekkac256()
    val contractName = "sikorka experiment"
    val basicInterface = SikorkaBasicInterface.deploy(
        deployData.transactOpts,
        deployData.ec,
        contractName,
        lat,
        long,
        contractData.question,
        answerHash.hexStringToByteArray()
    )
    Timber.v("preparing to deploy contract: {$contractName} " +
        "with latitude: ${lat.getString(10)}, longitude ${long.getString(10)} " +
        "question: ${contractData.question} => answer hash: $answerHash"
    )

    val address = basicInterface.address
    val deployer = basicInterface.deployer
    val pendingContract = PendingContract(
        contractAddress = address.hex,
        transactionHash = deployer!!.hash.hex
    )
    Timber.v("pending contract: $pendingContract")
    pendingContractDataSource.insert(pendingContract)
    basicInterface
  }.onErrorResumeNext {
    val message = it.message ?: ""
    val error = when {
      message.contains("could not decrypt key with given passphrase") -> InvalidPassphraseException(it)
      message.contains("exceeds block gas limit") -> ExceedsBlockGasLimit(it)
      else -> it
    }
    Single.error<SikorkaBasicInterface>(error)
  }

  fun bindSikorkaInterface(addressHex: String): Single<ISikorkaBasicInterface> =
      gethNode.ethereumClient().flatMap { ethereumClient ->
        Single.fromCallable {
          val address = Geth.newAddressFromHex(addressHex)
          val boundContract = SikorkaBasicInterface(address, ethereumClient)
          Timber.v("Question ${boundContract.question(null)} -> name ${boundContract.name(null)}")
          boundContract
        }
      }



  private fun bindRegistry() = gethNode.ethereumClient().map { SikorkaRegistry.bind(it) }

  private data class DeployData(val transactOpts: TransactOpts, val ec: EthereumClient)
}