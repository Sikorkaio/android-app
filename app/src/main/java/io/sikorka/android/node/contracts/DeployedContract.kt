package io.sikorka.android.node.contracts

data class DeployedContract(
    val addressHex: String,
    val latitude: Double,
    val longitude: Double
)


data class DeployedContractModel(val data: List<DeployedContract>)