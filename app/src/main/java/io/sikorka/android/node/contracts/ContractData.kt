package io.sikorka.android.node.contracts

data class ContractData(
    val gas: ContractGas,
    val question: String,
    val answer: String,
    val latitude: Double,
    val longitude: Double
)