package io.sikorka.test_geth.node.configuration

import org.ethereum.geth.Enodes
import org.ethereum.geth.NodeConfig
import java.io.File

/**
 * Provides basic configuration information for the Geth node to connect
 */
interface IConfiguration {
  val dataDir: File
  val nodeConfig: NodeConfig
  val bootstrapNodes: Enodes?
}