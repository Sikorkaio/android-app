package io.sikorka.android.node

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.sikorka.android.helpers.fail
import io.sikorka.android.node.accounts.AccountModel
import io.sikorka.android.node.configuration.ConfigurationProvider
import io.sikorka.android.node.configuration.IConfiguration
import io.sikorka.android.node.contracts.ContractGas
import org.ethereum.geth.*
import timber.log.Timber
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GethNode
@Inject
constructor(
    private val configurationProvider: ConfigurationProvider
) {
  private val ethContext = Geth.newContext()
  private var node: Node? = null
  private lateinit var configuration: IConfiguration

  private val disposables: CompositeDisposable = CompositeDisposable()
  private val loggingThrottler: PublishRelay<PeerInfos> = PublishRelay.create()

  private fun addDisposable(disposable: Disposable) {
    disposables.add(disposable)
  }

  fun start() {
    if (node != null) {
      return
    }

    configuration = configurationProvider.getActive()
    val dataDir = configuration.dataDir
    val nodeConfig = configuration.nodeConfig

    Timber.v("node data directory will be in $dataDir")
    node = Geth.newNode(dataDir.absolutePath, nodeConfig)
    val node = node ?: fail("what node?")
    node.start()
    periodicallyCheckIfRunning()
    addDisposable(
        loggingThrottler.throttleLast(1, TimeUnit.MINUTES)
            .subscribe { logPeers(it) }
    )
  }

  fun stop() {
    Timber.v("Stoping geth node.")
    try {
      val node = node ?: return
      node.stop()
    } catch (e: Exception) {
      Timber.v(e)
    }
    disposables.clear()
  }


  fun getBalance(address: Address): BigDecimal {
    val bigIntBalance = ethereumClient.getBalanceAt(ethContext, address, -1)
    return BigDecimal(bigIntBalance.getString(10))
  }

  fun createTransactOpts(
      account: AccountModel,
      gas: ContractGas,
      signer: TransactionSigner): Single<TransactOpts> {
    return Single.fromCallable {
      val signerAccount = account.ethAccount
      val signerAddress = signerAccount.address
      val opts = TransactOpts()
      opts.setContext(ethContext)
      opts.from = signerAddress
      opts.nonce = ethereumClient.getPendingNonceAt(ethContext, signerAddress)
      opts.setSigner({ address, transaction -> signer(address, transaction, chainId()) })
      opts.gasLimit = gas.limit
      opts.gasPrice = Geth.newBigInt(gas.price)
      return@fromCallable opts
    }
  }

  private fun chainId(): BigInt {
    val nodeConfig = configuration.nodeConfig
    return Geth.newBigInt(nodeConfig.ethereumNetworkID)
  }

  fun suggestedGasPrice(): Single<BigInt> = Single.fromCallable {
    val suggestGasPrice = ethereumClient.suggestGasPrice(ethContext)
    Timber.v("suggested gas price ${suggestGasPrice.getString(10)} wei")
    suggestGasPrice
  }

  fun ethereumClient(): Single<EthereumClient> = Single.fromCallable { ethereumClient }

  fun getReceipt(transactionHex: String): Single<Receipt> = Single.fromCallable {
    val hash = Geth.newHashFromHex(transactionHex)
    return@fromCallable ethereumClient.getTransactionReceipt(ethContext, hash)
  }.onErrorResumeNext {
    val message = it.message ?: ""
    val throwable = if (message.contains("not found", true)) {
      TransactionNotFoundException(transactionHex, it)
    } else {
      it
    }
    return@onErrorResumeNext Single.error<Receipt>(throwable)
  }

  private val ethereumClient: EthereumClient
    get() {
      checkNodeStatus()
      val ethNode = node ?: fail("no node")
      val ethereumClient = ethNode.ethereumClient
      return ethereumClient ?: fail("no client")
    }

  private fun checkNodeStatus() {
    if (node == null) {
      Timber.v("Node was null")
      start()
    }
  }

  private fun periodicallyCheckIfRunning() {
    addDisposable(Observable.interval(10, TimeUnit.MINUTES)
        .subscribe({
          checkNodeStatus()
        }) {
          Timber.v(it, "")
        })
  }

  private fun syncProgress(ec: EthereumClient): Pair<Long, Long> = try {
    val syncProgress = ec.syncProgress(ethContext)
    val currentBlock = ec.getBlockByNumber(ethContext, -1)
    val highestBlock = syncProgress?.highestBlock ?: currentBlock.number
    Pair(currentBlock.number, highestBlock)
  } catch (ex: Exception) {
    Timber.v(ex)
    Pair(0, 0)
  }

  fun canCallMethods(): Single<Boolean> = Single.fromCallable {
    ethereumClient.syncProgress(ethContext)
    ethereumClient.getBlockByNumber(ethContext, -1)
    return@fromCallable true
  }.onErrorResumeNext {
    val message = it.message ?: ""
    val error = if (message.contains("no suitable peers available")) {
      NoSuitablePeersAvailableException(it)
    } else {
      it
    }
    Single.error(error)
  }

  private fun headers(): Observable<Header> = Observable.create<Header> {
    val handler = object : NewHeadHandler {
      override fun onError(error: String) {
        Timber.v(error)
      }

      override fun onNewHead(header: Header) {
        it.onNext(header)
      }
    }
    ethereumClient.subscribeNewHead(ethContext, handler, 16)
  }

  fun status(): Observable<SyncStatus> = Observable.interval(2, TimeUnit.SECONDS)
      .flatMap { checkStatus() }
      .startWith(checkStatus())


  private fun checkStatus(): Observable<SyncStatus> = Observable.fromCallable {
    val ethNode = node ?: fail("node was null")
    val peers = ethNode.peersInfo
    val peerCount = peers.size().toInt()
    loggingThrottler.accept(peers)
    val (current, highest) = syncProgress(ethNode.ethereumClient)
    SyncStatus(peerCount, current, highest)
  }.onErrorReturn { SyncStatus(0, 0, 0) }

  private fun logPeers(peerInfos: PeerInfos?) {
    if (peerInfos == null) {
      return
    }
    Timber.v("Printing connected Peers\n========")
    peerInfos.all().forEach {
      Timber.v("Client => ${it.name} -- \"enode://${it.id}@${it.remoteAddress}\"")
    }
    Timber.v("========")
  }

  fun sendTransaction(transaction: Transaction) {
    ethereumClient.sendTransaction(ethContext, transaction)
  }
}

typealias TransactionSigner = (address: Address, transaction: Transaction, chainId: BigInt) -> Transaction
