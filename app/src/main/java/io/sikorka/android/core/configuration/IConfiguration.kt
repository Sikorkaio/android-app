package io.sikorka.android.core.configuration

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
  fun prepare()
}