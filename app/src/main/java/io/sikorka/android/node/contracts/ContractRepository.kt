package io.sikorka.android.node.contracts

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.sikorka.android.contract.ISikorkaBasicInterface
import io.sikorka.android.contract.SikorkaBasicInterface
import io.sikorka.android.contract.SikorkaBasicInterfacev011
import io.sikorka.android.contract.SikorkaRegistry
import io.sikorka.android.data.PendingContract
import io.sikorka.android.data.PendingContractDataSource
import io.sikorka.android.helpers.Lce
import io.sikorka.android.helpers.fail
import io.sikorka.android.helpers.hexStringToByteArray
import io.sikorka.android.helpers.sha3.kekkac256
import io.sikorka.android.io.StorageManager
import io.sikorka.android.node.*
import io.sikorka.android.node.accounts.AccountRepository
import io.sikorka.android.node.accounts.InvalidPassphraseException
import io.sikorka.android.utils.schedulers.SchedulerProvider
import org.ethereum.geth.EthereumClient
import org.ethereum.geth.Geth
import org.ethereum.geth.TransactOpts
import timber.log.Timber
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ContractRepository
@Inject
constructor(
    private val gethNode: GethNode,
    private val accountRepository: AccountRepository,
    private val pendingContractDataSource: PendingContractDataSource,
    private val storageManager: StorageManager,
    private val schedulerProvider: SchedulerProvider
) {

  fun getDeployedContracts(): Single<Lce<DeployedContractModel>> = gethNode.ethereumClient()
      .flatMap { ethereumClient ->
        return@flatMap Single.fromCallable {
          Timber.v("Binding registry contract")
          val sikorkaRegistry = SikorkaRegistry.bind(ethereumClient)

          val contractAddresses = sikorkaRegistry.getContractAddresses()
          val contractCoordinates = sikorkaRegistry.getContractCoordinates()

          val contractList = ArrayList<DeployedContract>()

          for (i in 0 until contractCoordinates.size() step 2) {
            val address = contractAddresses[i / 2]
            val modifier = BigDecimal(COORDINATES_MODIFIER)
            val latitude = BigDecimal(contractCoordinates[i].getString(10)).divide(modifier)
            val longitude = BigDecimal(contractCoordinates[i + 1].getString(10)).divide(modifier)
            val deployedContract = DeployedContract(address.hex, latitude.toDouble(), longitude.toDouble())
            contractList.add(deployedContract)
          }

          return@fromCallable Lce.success(DeployedContractModel(contractList))
        }.timeout(1, TimeUnit.MINUTES).onErrorReturn {
          return@onErrorReturn when {
            it.messageValue.contains("no suitable peers available") -> Lce.failure<DeployedContractModel>(NoSuitablePeersAvailableException(it))
            it.messageValue.contains("no contract code at given address") -> Lce.failure(NoContractCodeAtGivenAddressException(it))
            else -> Lce.failure(it)
          }
        }
      }


  fun deployContract(passphrase: String, contractData: ContractData): Single<SikorkaBasicInterface> {
    val sign = signer(passphrase, contractData.gas)
    return Single.zip(sign, gethNode.ethereumClient(), BiFunction { transactOpts: TransactOpts, ec: EthereumClient ->
      Timber.v("getting ready to deploy")
      DeployData(transactOpts, ec)
    }).flatMap { deploy(contractData, it, passphrase) }
  }

  fun deployDetectorContract(passphrase: String, data: DetectorContractData): Single<SikorkaBasicInterfacev011> {
    val signer = signer(passphrase, data.gas)
    return Single.zip(signer, gethNode.ethereumClient(), BiFunction { transactOpts: TransactOpts, ec: EthereumClient ->
      Timber.v("getting ready to deploy")
      DeployData(transactOpts, ec)
    }).flatMap { deploy(data, it, passphrase) }
  }

  private fun deploy(data: DetectorContractData, deployData: DeployData, passphrase: String): Single<SikorkaBasicInterfacev011> = Single.fromCallable {
    val latitude = Geth.newBigInt((data.latitude * COORDINATES_MODIFIER).toLong())
    val longitude = Geth.newBigInt((data.longitude * COORDINATES_MODIFIER).toLong())
    val secondsAllowed = Geth.newBigInt(data.secondsAllowed.toLong())
    val detector = Geth.newAddressFromHex(data.detectorAddress)
    val registry = Geth.newAddressFromHex(SikorkaRegistry.REGISTRY_ADDRESS)
    return@fromCallable SikorkaBasicInterfacev011.deploy(
        deployData.transactOpts,
        deployData.ec,
        data.name,
        detector,
        latitude,
        longitude,
        secondsAllowed,
        registry
    )
  }

  private fun deploy(contractData: ContractData, deployData: DeployData, passphrase: String): Single<SikorkaBasicInterface> = Single.fromCallable {
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
    val deployedContractInfo = DeployedContractInfo(address, lat, long)
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


  private fun signer(passphrase: String, gas: ContractGas): Single<TransactOpts> {
    return accountRepository.selectedAccount().flatMap {
      return@flatMap gethNode.createTransactOpts(it, gas) { _, transaction, chainId ->
        val signedTransaction = accountRepository.sign(it.addressHex, passphrase, transaction, chainId) ?: fail("null transaction was returned")
        Timber.v("signing ${transaction.hash.hex} ${transaction.cost} ${transaction.nonce}")
        signedTransaction
      }
    }
  }

  private fun bindRegistry() = gethNode.ethereumClient().map { SikorkaRegistry.bind(it) }

  private data class DeployData(val transactOpts: TransactOpts, val ec: EthereumClient)

  companion object {
    private const val COORDINATES_MODIFIER = 10_000_000_000_000_000
  }
}