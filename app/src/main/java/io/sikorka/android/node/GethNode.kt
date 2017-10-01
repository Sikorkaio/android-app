package io.sikorka.android.node

import io.reactivex.Single
import io.sikorka.android.events.RxBus
import io.sikorka.android.events.UpdateSyncStatusEvent
import io.sikorka.android.helpers.fail
import io.sikorka.android.node.accounts.AccountModel
import io.sikorka.android.node.configuration.ConfigurationFactory
import io.sikorka.android.node.configuration.IConfiguration
import io.sikorka.android.settings.AppPreferences
import org.ethereum.geth.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GethNode
@Inject
constructor(
    private val configurationFactory: ConfigurationFactory,
    private val appPreferences: AppPreferences,
    private val rxBus: RxBus
) {
  private val ethContext = Geth.newContext()
  private var node: Node? = null
  private var listener: ((String, Int) -> Unit)? = null
  private lateinit var configuration: IConfiguration

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
    schedulerPeerCheck(node)
  }


  fun getBalance(address: Address): BigInt {
    return ethereumClient.getBalanceAt(ethContext, address, -1)
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

  fun suggestedGasPrice(): Single<BigInt> {
    return Single.fromCallable { ethereumClient.suggestGasPrice(ethContext) }
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

  private fun schedulerPeerCheck(node: Node) {
    Timer().scheduleAtFixedRate(object : TimerTask() {
      override fun run() {

        val peerInfos = node.peersInfo
        val peers = peerInfos.size().toInt()
        logPeers(peerInfos)
        var message = "Peers: $peers"
        val (current, highest) = syncProgress(node.ethereumClient)
        if (highest > 0) {
          message = "$message | Block $current of $highest"
        }
        Timber.v("checking sync progress $message")
        listener?.invoke(message, peers)
        val status = SyncStatus(peers, current, highest)
        rxBus.post(UpdateSyncStatusEvent(status))
        val pendingTransactionCount = ethereumClient.getPendingTransactionCount(ethContext)
        Timber.v("pending transactions $pendingTransactionCount")
      }

      private fun logPeers(peerInfos: PeerInfos?) {
        if (peerInfos == null) {
          return
        }
        for (peer in peerInfos) {
          Timber.v("Connected to ${peer.name} on ${peer.remoteAddress} (${peer.id})")
        }
      }

      operator fun PeerInfos.iterator(): Iterator<PeerInfo> = object : Iterator<PeerInfo> {
        var current = 0L
        override fun hasNext(): Boolean {
          return current < size()
        }

        override fun next(): PeerInfo {
          return get(current++)
        }

      }


    }, 0, 30000)//put here time 1000 milliseconds=1 second
  }
}

typealias TransactionSigner = (address: Address, transaction: Transaction, chainId: BigInt) -> Transaction
