package io.sikorka.android.node.contracts.data

data class ContractData(
    override val name: String,
    override val gas: ContractGas,
    override val latitude: Double,
    override val longitude: Double
) : IContractData