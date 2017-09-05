package io.sikorka.android.node

import io.sikorka.android.helpers.fail
import io.sikorka.android.node.configuration.ConfigurationFactory
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
    private val appPreferences: AppPreferences
) {
  private val ethContext = Geth.newContext()
  private var node: Node? = null
  private var listener: ((String, Int) -> Unit)? = null

  fun setListener(listener: (String, Int) -> Unit) {
    this.listener = listener
  }

  fun start() {
    if (node != null) {
      return
    }

    val configuration = configurationFactory.configuration(appPreferences.selectedNetwork())
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
    val ethNode = node ?: fail("no node")
    return ethNode.ethereumClient.getBalanceAt(ethContext, address, -1)
  }

  private fun syncProgress(ec: EthereumClient): String {
    return try {
      val syncProgress = ec.syncProgress(ethContext)
      "block: ${ec.getBlockByNumber(ethContext, -1).number} of ${syncProgress.highestBlock}"
    } catch (ex: Exception) {
      Timber.v(ex)
      ""
    }
  }

  private fun schedulerPeerCheck(node: Node) {
    Timer().scheduleAtFixedRate(object : TimerTask() {
      override fun run() {

        val peerInfos = node.peersInfo
        var message = "Peers: ${peerInfos.size()}"
        val syncProgress = syncProgress(node.ethereumClient)
        if (syncProgress.isNotBlank()) {
          message = "$message - $syncProgress"
        }
        Timber.v("checking sync progress $message")
        listener?.invoke(message, peerInfos.size().toInt())
      }

    }, 0, 30000)//put here time 1000 milliseconds=1 second
  }
}