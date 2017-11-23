package io.sikorka.android.core.contracts.data

data class DeployedContract(
    val addressHex: String,
    val latitude: Double,
    val longitude: Double
)

data class DeployedContractModel(val data: List<DeployedContract>)