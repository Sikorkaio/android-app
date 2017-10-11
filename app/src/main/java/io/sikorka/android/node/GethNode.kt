package io.sikorka.android.node

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.sikorka.android.events.RxBus
import io.sikorka.android.helpers.fail
import io.sikorka.android.node.accounts.AccountModel
import io.sikorka.android.node.configuration.ConfigurationFactory
import io.sikorka.android.node.configuration.IConfiguration
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.utils.schedulers.SchedulerProvider
import org.ethereum.geth.*
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GethNode
@Inject
constructor(
    private val configurationFactory: ConfigurationFactory,
    private val appPreferences: AppPreferences,
    private val rxBus: RxBus,
    private val schedulerProvider: SchedulerProvider
) {
  private val ethContext = Geth.newContext()
  private var node: Node? = null
  private var listener: ((String, Int) -> Unit)? = null
  private lateinit var configuration: IConfiguration

  private val disposables: CompositeDisposable = CompositeDisposable()
  private fun addDisposable(disposable: Disposable) {
    disposables.add(disposable)
  }

  fun setListener(listener: (String, Int) -> Unit) {
    this.listener = listener
  }

  fun start() {
    if (node != null) {
      return
    }

    configuration = configurationFactory.configuration(appPreferences.selectedNetwork())
    configuration.prepare()
    val dataDir = configuration.dataDir
    val nodeConfig = configuration.nodeConfig

    Timber.v("node data directory will be in $dataDir")
    node = Geth.newNode(dataDir.absolutePath, nodeConfig)
    val node = node ?: fail("what node?")
    node.start()

    addDisposable(headers()
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.io())
        .subscribe({
          Timber.v("new header ${it.number} - ${it.hash.hex}")
        }) {
          Timber.v(it)
        }
    )
  }


  fun stop() {
    val node = node ?: return

    node.stop()
    disposables.clear()
  }


  fun getBalance(address: Address): BigDecimal {
    val bigIntBalance = ethereumClient.getBalanceAt(ethContext, address, -1)
    return BigDecimal(bigIntBalance.getString(10))
  }

  fun createTransactOpts(
      account: AccountModel,
      gasPrice: Long,
      gasLimit: Long,
      signer: TransactionSigner): Single<TransactOpts> {
    return Single.fromCallable {
      val signerAccount = account.ethAccount
      val signerAddress = signerAccount.address
      val opts = TransactOpts()
      opts.setContext(ethContext)
      opts.from = signerAddress
      opts.nonce = ethereumClient.getPendingNonceAt(ethContext, signerAddress)
      opts.setSigner({ address, transaction -> signer(address, transaction, chainId()) })
      opts.gasLimit = gasLimit
      opts.gasPrice = Geth.newBigInt(gasPrice)
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

  private val ethereumClient: EthereumClient
    get() {
      val ethNode = node ?: fail("no node")
      val ethereumClient = ethNode.ethereumClient
      return ethereumClient ?: fail("no client")
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

  fun status(): Observable<SyncStatus> = headers()
      .flatMap { checkStatus() }
      .startWith(checkStatus())

  private fun checkStatus(): Observable<SyncStatus> = Observable.fromCallable {
    val ethNode = node ?: fail("node was null")

    val peers = ethNode.peersInfo
    val peerCount = peers.size().toInt()
    logPeers(peers)
    var message = "Peers: $peerCount"
    val (current, highest) = syncProgress(ethNode.ethereumClient)
    if (highest > 0) {
      message = "$message | Block $current of $highest"
    }
    Timber.v("Sync progress $message")
    listener?.invoke(message, peerCount)
    SyncStatus(peerCount, current, highest)

  }


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
}

typealias TransactionSigner = (address: Address, transaction: Transaction, chainId: BigInt) -> Transaction
