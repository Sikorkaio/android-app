package io.sikorka.android.core.contracts.model

data class DeployedContract(
  val addressHex: String,
  val latitude: Double,
  val longitude: Double,
  val contractName: String
)

data class DeployedContractModel(val data: List<DeployedContract>)