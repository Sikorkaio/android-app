package io.sikorka.android.core.configuration

import android.os.Environment
import org.ethereum.geth.Enodes
import org.ethereum.geth.Geth
import org.ethereum.geth.NodeConfig
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RopstenConfiguration
@Inject
constructor(private val peerHelper: PeerHelper) : IConfiguration {

  private val dataDirectory: File by lazy {
    File(Environment.getExternalStorageDirectory(), "/$directoryPath")
  }

  override val peerFilePath = "${dataDir.absolutePath}/GethDroid/static-nodes.json"

  override fun prepare() {
    prepareDataDir(dataDir)
    peerHelper.prepareStaticNodes(peerFilePath, assetFilename)
  }

  override val nodeConfig: NodeConfig
    get() = Geth.newNodeConfig().apply {
      ethereumEnabled = true
      whisperEnabled = true
      ethereumGenesis = Geth.testnetGenesis()
      ethereumNetworkID = 3
      maxPeers = 30
      ethereumDatabaseCache = 32
      bootstrapNodes = bootstrapNodes
    }

  override val bootstrapNodes: Enodes?
    get() {
      val node = "enode://20c9ad97c081d63397d7b685a412227a40e23c8bdc6688c6f37e97cfbc22d2b4" +
        "d1db1510d8f61e6a8866ad7f0e17c02b14182d37ea7c3c8b9c2683aeb6b733a1@52.169.14.227:30303"
      val node2 = "enode://6ce05930c72abc632c58e2e4324f7c7ea478cec0ed4fa2528982cf34483094e9" +
        "cbc9216e7aa349691242576d552a2a56aaeae426c5303ded677ce455ba1acd9d@13.84.180.240:30303"

      return with(arrayListOf(node, node2)) {
        val enodes = Geth.newEnodes(2)

        forEachIndexed { index, node ->
          enodes.set(index.toLong(), Geth.newEnode(node))
        }

        enodes
      }
    }

  override val dataDir: File
    get() = dataDirectory

  override val network: Int = Network.ROPSTEN

  companion object {
    const val directoryPath = ".ropsten"
    const val assetFilename = "ropsten_peers.json"
  }
}