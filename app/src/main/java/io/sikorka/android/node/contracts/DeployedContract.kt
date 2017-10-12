package io.sikorka.android.node.contracts

import org.ethereum.geth.Address
import org.ethereum.geth.BigInt

data class DeployedContract(
    val addressHex: String,
    val latitude: Double,
    val longitude: Double
)

data class DeployedContractModel(val data: List<DeployedContract>)

data class DeployedContractInfo(
    val address: Address,
    val latitude: BigInt,
    val longitude: BigInt
) {

  override fun toString(): String = "deployed contract: ${address.hex} @ $latitude,$longitude"
}