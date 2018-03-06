package io.sikorka.android.core.contracts.model

data class ContractData(
  override val name: String,
  override val gas: ContractGas,
  override val latitude: Double,
  override val longitude: Double,
  override val totalSupply: Long
) : IContractData