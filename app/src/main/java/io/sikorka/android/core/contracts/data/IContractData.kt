package io.sikorka.android.core.contracts.data

interface IContractData {
  val name: String
  val gas: ContractGas
  val latitude: Double
  val longitude: Double
  val totalSupply: Long
}