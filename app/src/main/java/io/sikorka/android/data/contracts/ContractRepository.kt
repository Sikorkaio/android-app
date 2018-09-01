package io.sikorka.android.data.contracts

import androidx.lifecycle.LiveData
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
import io.sikorka.android.contract.DiscountContract
import io.sikorka.android.contract.SikorkaRegistry
import io.sikorka.android.core.GethNode
import io.sikorka.android.core.accounts.AccountRepository
import io.sikorka.android.core.contracts.model.ContractGas
import io.sikorka.android.core.contracts.model.DetectorContractData
import io.sikorka.android.core.contracts.model.IContractData
import io.sikorka.android.data.contracts.deployed.DeployedSikorkaContract
import io.sikorka.android.data.contracts.deployed.DeployedSikorkaContractDao
import io.sikorka.android.data.contracts.pending.PendingContract
import io.sikorka.android.data.contracts.pending.PendingContractDao
import org.ethereum.geth.Address
import org.ethereum.geth.BigInt
import org.ethereum.geth.EthereumClient
import org.ethereum.geth.Geth
import org.ethereum.geth.TransactOpts
import org.ethereum.geth.Transaction
import org.threeten.bp.Instant
import timber.log.Timber
import java.math.BigDecimal

class ContractRepository(
  private val gethNode: GethNode,
  private val accountRepository: AccountRepository,
  private val pendingContractDao: PendingContractDao,
  private val deployedSikorkaContractDao: DeployedSikorkaContractDao
) {

  fun getDeployedContracts(): LiveData<List<DeployedSikorkaContract>> {
    return deployedSikorkaContractDao.getDeployedContracts()
  }

  fun deployContract(passphrase: String, data: IContractData): Single<DiscountContract> {
    val signer = signer(passphrase, data.gas)
    val client = gethNode.ethereumClient()

    return Singles.zip(signer, client) { opts: TransactOpts, ec: EthereumClient ->
      Timber.v("getting ready to deploy")
      DeployData(opts, ec)
    }.flatMap { deploy(data, it) }
  }

  private fun deploy(data: IContractData, deployData: DeployData): Single<DiscountContract> {
    return Single.fromCallable {
      val modifier = BigDecimal(COORDINATES_MODIFIER)
      val biLatitude = BigDecimal(data.latitude).multiply(modifier).toBigInteger()
      val biLogitude = BigDecimal(data.longitude).multiply(modifier).toBigInteger()

      val latitude = Geth.newBigInt(0).apply {
        setString(biLatitude.toString(10), 10)
      }

      val longitude = Geth.newBigInt(0).apply {
        setString(biLogitude.toString(10), 10)
      }

      val totalSupply = Geth.newBigInt(data.totalSupply)

      Timber.v("Settings lat: ${latitude.string()}, long: ${longitude.string()}")

      val secondsAllowed: BigInt?
      val detector: Address?
      if (data is DetectorContractData) {
        secondsAllowed = Geth.newBigInt(data.secondsAllowed.toLong())
        detector = Geth.newAddressFromHex(data.detectorAddress)
      } else {
        secondsAllowed = Geth.newBigInt(0)
        detector = Geth.newAddressFromHex("0000000000000000000000000000000000000000")
      }

      val registry = Geth.newAddressFromHex(SikorkaRegistry.REGISTRY_ADDRESS)
      val contract = DiscountContract.deploy(
        deployData.transactOpts,
        deployData.ec,
        data.name,
        detector,
        latitude,
        longitude,
        secondsAllowed,
        registry,
        totalSupply
      )

      val address = contract.address
      val deployer = contract.deployer
      val pendingContract = PendingContract(
        contractAddress = address.hex,
        transactionHash = deployer!!.hash.hex,
        dateCreated = Instant.now().epochSecond
      )
      Timber.v("pending contract: $pendingContract")
      pendingContractDao.insert(pendingContract)
      return@fromCallable contract
    }
  }

  fun bindSikorkaInterface(addressHex: String): Single<DiscountContract> =
    gethNode.ethereumClient().flatMap { ethereumClient ->
      Single.fromCallable {
        val address = Geth.newAddressFromHex(addressHex)
        val boundContract = DiscountContract(address, ethereumClient)
        Timber.v("contract -> name ${boundContract.name()}")
        boundContract
      }
    }

  private fun signer(passphrase: String, gas: ContractGas): Single<TransactOpts> {
    return accountRepository.selectedAccount().flatMap {
      return@flatMap gethNode.createTransactOpts(it, gas) { _, transaction, chainId ->
        val signedTransaction = checkNotNull(accountRepository.sign(
          it.addressHex,
          passphrase,
          transaction,
          chainId
        )) { "null transaction was returned" }
        Timber.v("signing ${transaction.hash.hex} ${transaction.cost} ${transaction.nonce}")
        signedTransaction
      }
    }
  }

  private data class DeployData(val transactOpts: TransactOpts, val ec: EthereumClient)

  fun transact(
    function: (opts: TransactOpts) -> Transaction,
    passphrase: String,
    gas: ContractGas
  ): Single<Transaction> {
    return signer(passphrase, gas).map { function(it) }
  }

  companion object {
    const val COORDINATES_MODIFIER = 10_000_000_000_000_000
  }
}