package io.sikorka.android.node.contracts

data class DetectorContractData(
    val name: String,
    val gas: ContractGas,
    val detectorAddress: String,
    val secondsAllowed: Int,
    val latitude: Double,
    val longitude: Double
)